package com.iflytek.tps.service.client;

import com.google.protobuf.ByteString;
import com.iflytek.tps.beans.core.IstGrpc;
import com.iflytek.tps.beans.core.IstOuterClass;
import com.iflytek.tps.beans.exception.AppException;
import com.iflytek.tps.beans.transfer.IstSessionParam;
import com.iflytek.tps.beans.transfer.IstSessionResponse;
import com.iflytek.tps.beans.transfer.IstSessionResult;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class IstClient implements AutoCloseable{

    private static Logger logger = LoggerFactory.getLogger(IstClient.class);

    /**
     * ist server 端 ip:port, 例如：127.0.0.1:1234
     */
    private String url;

    /**
     * 请求 ist 的会话参数
     */
    private IstSessionParam istSessionParam;

    /**
     * client 回调
     */
    private IstSessionResponse istSessionResponse;

    /**
     * grpc client 端请求类
     */
    private IstGrpc.IstStub stub;

    /* channel */
    private ManagedChannel channel;

    /**
     * grpc 请求流
     */
    private StreamObserver<IstOuterClass.IstRequest> requestStreamObserver;

    /**
     * grpc 结果返回流
     */
    private StreamObserver<IstOuterClass.IstResult> resultStreamObserver;


    /**
     * IstClient 带参构造函数
     *
     * @param url   server 的 ip:port, 例如：192.168.0.1:1234
     * @param param 和引擎会话参数
     */
    public IstClient(String url, IstSessionParam param) {
        this.url = url;
        this.istSessionParam = param;
    }

    /**
     * 连接引擎方法
     *
     * @param _istSessionResponse 引擎返回结果回调
     * @return 连接是否成功标志
     */
    public boolean connect(IstSessionResponse _istSessionResponse) {

        logger.info("sid:{}, start connect", istSessionParam.getSid());
        this.istSessionResponse = _istSessionResponse;
        this.resultStreamObserver = new StreamObserver<IstOuterClass.IstResult>() {
            @Override
            public void onNext(IstOuterClass.IstResult istResult) {
                if (istResult != null && istSessionResponse != null) {
                    IstSessionResult istSessionResult = new IstSessionResult();
                    istSessionResult.setAnsStr(istResult.getAnsStr());
                    istSessionResult.setEndFlag(istResult.getEndFlag());
                    istSessionResult.setErrCode(istResult.getErrCode());
                    istSessionResult.setErrStr(istResult.getErrStr());
                    istSessionResponse.onCallback(istSessionResult);
                } else {
                    logger.error("sid:{},istResult or istSessionResponse is null, error", istSessionParam.getSid());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (istSessionResponse != null) {
                    istSessionResponse.onError(throwable);
                } else {
                    logger.error("sid:{}, istSessionResponse is null, error", istSessionParam.getSid());
                }
            }

            @Override
            public void onCompleted() {
                if (istSessionResponse != null) {
                    istSessionResponse.onCompleted();
                } else {
                    logger.error("sid:{}, istSessionResponse is null, error", istSessionParam.getSid());
                }

            }
        };

        boolean ret = false;
        try {
            channel = ManagedChannelBuilder.forTarget(this.url).usePlaintext().build();
            this.stub = IstGrpc.newStub(channel);

            // 第一次连接 server 端,传递会话参数
            postConnect();
            ret = true;
        } catch (Exception e) {
            logger.error("sid:{}, connect error", istSessionParam.getSid(), e);
            return ret;
        }
        return ret;
    }

    /**
     * connect 连接服务的私有方法
     *
     * @throws AppException 连接异常
     */
    private void postConnect() throws AppException {
        requestStreamObserver = this.stub.createRec(resultStreamObserver);

        try {
            Map<String, String> sessionParam = new HashMap<String, String>();
            sessionParam.put("sid", istSessionParam.getSid());
            sessionParam.put("aue", istSessionParam.getAue());
            sessionParam.put("rst", istSessionParam.getRst());
            sessionParam.put("rse", istSessionParam.getRse());
            sessionParam.put("eos", istSessionParam.getEos());
            sessionParam.put("dwa", istSessionParam.getDwa());
            sessionParam.put("rate", istSessionParam.getRate());
            sessionParam.put("hotword", istSessionParam.getHotword());

            IstOuterClass.IstRequest request = IstOuterClass.IstRequest.newBuilder().putAllSessionParam(sessionParam).setSamplesInfo(this.istSessionParam.getSamplesInfo()).setEndFlag(false).build();

            logger.info("sid:{}, first transter session params", istSessionParam.getSid());
            requestStreamObserver.onNext(request);
        } catch (Exception e) {
            logger.error("sid:{}, postConnect error", istSessionParam.getSid(), e);
            requestStreamObserver.onError(e);
            throw new AppException("requestStreamObserver.onNext() error");
        }
    }

    /**
     * 向引擎发送数据
     *
     * @param bytes 待识别音频数据
     */
    public synchronized void post(byte bytes[]) {
        try {
            ByteString samples = ByteString.copyFrom(bytes);
            IstOuterClass.IstRequest request = IstOuterClass.IstRequest.newBuilder().setSamples(samples).setSamplesInfo(this.istSessionParam.getSamplesInfo()).setEndFlag(false).build();
            logger.debug("sid:{}, transter audio data Fragmentation", istSessionParam.getSid());
            requestStreamObserver.onNext(request);
        } catch (Exception e) {
            logger.error("sid:{}, post error", istSessionParam.getSid(), e);
            requestStreamObserver.onError(e);
        }
    }

    /**
     * 和引擎会话结束,断开会话
     */
    public void end() {
        try {
            IstOuterClass.IstRequest request = IstOuterClass.IstRequest.newBuilder().setSamplesInfo(this.istSessionParam.getSamplesInfo()).setEndFlag(true).build();
            logger.info("sid:{}, the last transter, EndFlag is true", istSessionParam.getSid());
            requestStreamObserver.onNext(request);
        } catch (Exception e) {
            logger.info("sid:{}, poseEnd error", istSessionParam.getSid(), e);
            requestStreamObserver.onError(e);
        }
        requestStreamObserver.onCompleted();
    }

    public String getUrl() {
        return this.url;
    }

    public IstSessionParam getIstSessionParam() {
        return this.istSessionParam;
    }

    public IstSessionResponse getIstSessionResponse() {
        return this.istSessionResponse;
    }

    @Override
    public void close() {
        try {
            channel.shutdown();
        } catch (Exception e) {
            logger.error("sid:{}, close channel failed!", istSessionParam.getSid(), e);
        }
    }
}
