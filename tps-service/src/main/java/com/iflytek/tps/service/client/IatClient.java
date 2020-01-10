package com.iflytek.tps.service.client;

import com.google.protobuf.ByteString;
import com.iflytek.tps.beans.core.Dictation;
import com.iflytek.tps.beans.core.IatGrpc;
import com.iflytek.tps.beans.dictation.IatSessionParam;
import com.iflytek.tps.beans.dictation.IatSessionResponse;
import com.iflytek.tps.beans.dictation.IatSessionResult;
import com.iflytek.tps.beans.exception.AppException;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.shaded.io.netty.util.internal.StringUtil;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class IatClient implements AutoCloseable{

    private final static Logger logger = LoggerFactory.getLogger(IatClient.class);

    /**
     * url 由 ip:port 组成,例如：127.0.0.1:8080
     */
    private String url;

    /**
     * 与 iat 引擎交互会话参数.
     */
    private IatSessionParam iatSessionParam;

    /**
     * The stub.
     */
    private IatGrpc.IatStub stub;

    /* channel */
    private ManagedChannel channel;

    /**
     * 引擎结果回调.
     */
    private IatSessionResponse iatSessionResponse;

    /**
     * grpc 结果返回流
     */
    private StreamObserver<Dictation.IatResult> resultStreamObserver;

    /**
     * grpc 请求流
     */
    private StreamObserver<Dictation.IatRequest> requestStreamObserver;

    /**
     * Iat 客户端
     *
     * @param url             server ip:port ,例如：192.168.0.1:1234
     * @param iatSessionParam 和引擎会话参数
     */
    public IatClient(String url, IatSessionParam iatSessionParam) {
        this.url = url;
        this.iatSessionParam = iatSessionParam;
    }

    /**
     * 连接引擎开始会话
     *
     * @param iatSessionResponse 引擎结果回调
     * @return 是否连接成功
     */
    public boolean connect(IatSessionResponse iatSessionResponse) {

        logger.info("sid:{}, start connect", iatSessionParam.getSid());
        this.iatSessionResponse = iatSessionResponse;
        this.resultStreamObserver = new StreamObserver<Dictation.IatResult>() {

            @Override
            public void onNext(Dictation.IatResult iatResult) {
                if (iatResult != null && iatSessionResponse != null) {
                    IatSessionResult iatSessionResult = new IatSessionResult();
                    iatSessionResult.setAnsStr(iatResult.getAnsStr());
                    iatSessionResult.setEndFlag(iatResult.getEndFlag());
                    iatSessionResult.setErrCode(iatResult.getErrCode());
                    iatSessionResult.setErrStr(iatResult.getErrStr());
                    iatSessionResponse.onCallback(iatSessionResult);
                } else {
                    logger.error("sid:{},iatResult or iatSessionResponse is null, error", iatSessionParam.getSid());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (iatSessionResponse != null) {
                    iatSessionResponse.onError(throwable);
                } else {
                    logger.error("sid:{}, iatSessionResponse is null, error", iatSessionParam.getSid());
                }
            }

            @Override
            public void onCompleted() {
                if (iatSessionResponse != null) {
                    iatSessionResponse.onCompleted();
                } else {
                    logger.error("sid:{}, iatSessionResponse is null, error", iatSessionParam.getSid());
                }
            }
        };

        boolean ret = false;
        try {
            channel = ManagedChannelBuilder.forTarget(this.url).usePlaintext().build();
            this.stub = IatGrpc.newStub(channel);

            // 先连接一次,把会话参数传过去,后续连接可不用传递会话参数.
            postConnect();

            ret = true;
        } catch (Exception e) {
            logger.error("sid:{}, connect error", iatSessionParam.getSid(), e);
            return ret;
        }

        return ret;
    }

    /**
     * connect() 连接引擎,私有方法
     *
     * @throws AppException 连接异常
     */
    private void postConnect() throws AppException {
        requestStreamObserver = this.stub.createRec(resultStreamObserver);
        try {

            // 创建会话参数,只要求传一次,后续持续向服务端写音频时可以忽略（服务也不再解析）
            Map<String, String> sessionParam = new HashMap<String, String>();
            sessionParam.put("sid", iatSessionParam.getSid()); // 会话id,这个参数一定要传,方便以后检索日志
            sessionParam.put("aue", iatSessionParam.getAue()); // raw(pcm音频),speex,speex-wb,opus,音频编码格式,未传入默认speex-web
            sessionParam.put("rst", iatSessionParam.getRst()); // json,plain 返回结果的格式,未传入默认json
            sessionParam.put("rse", iatSessionParam.getRse()); // utf8,gbk 转写结果的编码,未传入默认utf8
            if (!StringUtil.isNullOrEmpty(iatSessionParam.getEos())) {
                sessionParam.put("eos", iatSessionParam.getEos()); // [0~600000] 引擎参数,vad后断点值,整数,毫秒,取值范围[0-60000]
            }
            if (!StringUtil.isNullOrEmpty(iatSessionParam.getDwa())) {
                sessionParam.put("dwa", iatSessionParam.getDwa()); // wpgs 引擎参数,是否获取中间结果
            }
            if (!StringUtil.isNullOrEmpty(iatSessionParam.getWbest())) {
                sessionParam.put("wbest", iatSessionParam.getWbest()); // [0~5] 引擎参数,多候选（句子级）
            }
            if (!StringUtil.isNullOrEmpty(iatSessionParam.getWbestonlyper())) {
                sessionParam.put("wbestonlyper", iatSessionParam.getWbestonlyper()); // true,false 引擎参数,是否只有人名多候选
            }
            sessionParam.put("rate", iatSessionParam.getRate()); // 8k,16k 采样率,如果传入的不是16k,则按照16k重采样
            if (!StringUtil.isNullOrEmpty(iatSessionParam.getHotword())) {
                sessionParam.put("hotword", iatSessionParam.getHotword()); // word1;word2;... 采用;分割的热词，为utf-8编码
            }

            Dictation.IatRequest request = Dictation.IatRequest.newBuilder().putAllSessionParam(sessionParam).setSamplesInfo(iatSessionParam.getSamplesInfo()).setEndFlag(false).build();

            logger.info("sid:{}, first transter session params", iatSessionParam.getSid());
            requestStreamObserver.onNext(request);
        } catch (Exception e) {
            logger.error("sid:{}, postConnect error", iatSessionParam.getSid(), e);
            requestStreamObserver.onError(e);
            throw new AppException("requestStreamObserver.onNext() error");
        }
    }

    /**
     * 向引擎发送数据接口
     *
     * @param bytes 发送给引擎的数据
     */
    public synchronized void post(byte[] bytes) {
        try {
            ByteString samples = ByteString.copyFrom(bytes);
            Dictation.IatRequest request = Dictation.IatRequest.newBuilder().setSamples(samples).setSamplesInfo(iatSessionParam.getSamplesInfo()).setEndFlag(false).build();
            //logger.debug("sid:{}, transter audio data Fragmentation", iatSessionParam.getSid());
            requestStreamObserver.onNext(request);
        } catch (Exception e) {
            logger.error("sid:{}, post error", iatSessionParam.getSid(), e);
            requestStreamObserver.onError(e);
        }
    }

    /**
     * 和引擎会话结束,断开会话连接
     */
    public void end() {
        try {
            Dictation.IatRequest request = Dictation.IatRequest.newBuilder().setSamplesInfo(iatSessionParam.getSamplesInfo()).setEndFlag(true).build();
            logger.info("sid:{}, the last transter, EndFlag is true", iatSessionParam.getSid());
            requestStreamObserver.onNext(request);
        } catch (Exception e) {
            logger.info("sid:{}, poseEnd error", iatSessionParam.getSid(), e);
            requestStreamObserver.onError(e);
        }
        requestStreamObserver.onCompleted();
    }

    /**
     * Gets the url 由 ip:port 组成,例如：127.0.0.1:1234
     *
     * @return the url 由 ip:port 组成,例如：127.0.0.1:1234
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets the 与 iat 引擎交互会话参数.
     *
     * @return the 与 iat 引擎交互会话参数
     */
    public IatSessionParam getIatSessionParam() {
        return iatSessionParam;
    }

    @Override
    public void close() throws Exception {
        try {
            channel.shutdown();
        } catch (Exception e) {
            logger.error("sid:{}, close channel failed!", iatSessionParam.getSid(), e);
        }

    }

    public IatSessionResponse getIatSessionResponse() {
        return iatSessionResponse;
    }

}
