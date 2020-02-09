package com.iflytek.tps.service.impl;

import com.iflytek.tps.beans.common.Commons;
import com.iflytek.tps.beans.dictation.IatSessionResponse;
import com.iflytek.tps.beans.dictation.IatSessionResult;
import com.iflytek.tps.foun.dto.HttpClientResult;
import com.iflytek.tps.foun.util.HttpClientUtils;
import com.iflytek.tps.service.request.CallBackRequest;
import com.iflytek.tps.service.util.IatFormatSentence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IatSessionResponseImpl implements IatSessionResponse {

    private String sid;//音频sid
    private Integer isLast;//采样率，8/16
    private String callBackUrl;//回调地址

    public IatSessionResponseImpl(String sid,String callBackUrl,Integer isLast){
        this.sid = sid;
        this.callBackUrl =callBackUrl;
        this.isLast = isLast;
    }

    private Logger logger = LoggerFactory.getLogger(IatSessionResponseImpl.class);

    @Override
    public void onCallback(IatSessionResult iatSessionResult) {
        logger.info("当前解析 sid: {}的音频信息",sid);
        logger.info("code:{}", iatSessionResult.getErrCode());
        logger.info("str:{}", iatSessionResult.getAnsStr());
        logger.info("flag:{}", iatSessionResult.isEndFlag());
        String sentence = IatFormatSentence.formatSentence(iatSessionResult.getAnsStr());
        logger.info("sentence:{}",sentence);
        Commons.IAT_RESULT.add(sentence);
        if(iatSessionResult.isEndFlag() || isLast == 1){//如果已经解析完成
            StringBuffer buffer = new StringBuffer();
            Commons.IAT_RESULT.stream().forEach(e->buffer.append(e));
            String sidres = sid;
            String resultStr = buffer.toString();
            System.out.println("当前解析结果："+resultStr);
//            //TODO 发送回调接口
            logger.info("开始进行第一次回调接口发送,发送内容为 sid{},restult {}",sidres,resultStr);
            HttpClientResult result = doPost(sidres,resultStr);//第一次发送
            logger.info("第一次调用结果："+result.toString());
            if(result.getCode() != 200 || result == null){//如果不等于200
                logger.info("开始进行第二次回调接口发送,发送内容为 sid{},restult {}",sidres,resultStr);
                result = doPost(sidres,resultStr);//发送第二次
                logger.info("第二次调用结果："+result.toString());
                if (result.getCode() != 200 || result == null){
                    logger.info("开始进行第三次回调接口发送,发送内容为 sid{},restult {}",sidres,resultStr);
                    result = doPost(sidres,resultStr);//发送第三次
                    logger.info("第三次调用结果："+result.toString());
                    Commons.IAT_RESULT.clear();
                }
            }
            //发送三次后，无论正确与否，释放内存内容
            Commons.IAT_RESULT.clear();
        }

    }


    private HttpClientResult doPost(String sid,String result){
        CallBackRequest request = new CallBackRequest();
        request.setSid(sid);
        request.setResult(result);
        HttpClientResult httpClientResult =null;
        try {
            httpClientResult = HttpClientUtils.doPost(callBackUrl,request);

        }catch (Exception e){
          logger.error("调用回调接口错误，错误message{}",e.getMessage());
        }
        return httpClientResult;
    }

    @Override
    public void onError(Throwable throwable) {
       logger.error("当前sid {}音频异常，异常信息：{}",sid,throwable.getMessage());
    }

    @Override
    public void onCompleted() {
      logger.info("当前解析 sid {}的音频信息解析已完成",sid);
    }
}
