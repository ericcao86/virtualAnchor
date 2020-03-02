package com.iflytek.tps.service.impl;

import com.iflytek.tps.beans.dictation.IatSessionResponse;
import com.iflytek.tps.beans.dictation.IatSessionResult;
import com.iflytek.tps.foun.dto.HttpClientResult;
import com.iflytek.tps.foun.util.HttpClientUtils;
import com.iflytek.tps.service.request.CallBackRequest;
import com.iflytek.tps.service.util.IatFormatSentence;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;


public class IatSessionResponseImpl implements IatSessionResponse {

    private String sid;//音频sid
    private Integer isLast;//采样率，8/16
    private String callBackUrl;//回调地址
    private Session session;

    public IatSessionResponseImpl(String sid,String callBackUrl,Integer isLast){
        this.sid = sid;
        this.callBackUrl =callBackUrl;
        this.isLast = isLast;
    }
    public IatSessionResponseImpl(Session session,String sid){
        this.session = session;
        this.sid = sid;
    }

    private static Logger logger = LoggerFactory.getLogger(IatSessionResponseImpl.class);

    @Override
    public void onCallback(IatSessionResult iatSessionResult) {
        logger.info("开始返回解析信息 sid {}",sid);
        logger.info("code:{}", iatSessionResult.getErrCode());
        logger.info("str:{}", iatSessionResult.getAnsStr());
        logger.info("flag:{}", iatSessionResult.isEndFlag());
        String sentence = IatFormatSentence.formatSentence(iatSessionResult.getAnsStr());
        logger.info("sentence:{}",sentence);
        if(iatSessionResult.isEndFlag() && StringUtils.isNotBlank(sentence)){//如果已经解析完成
            try {
                logger.info("开始往客户端推送信息 {}",sentence);
                session.getBasicRemote().sendText(sentence);
                logger.info("推送客户端信息完成 {}",sentence);
            } catch (Exception e) {
                logger.error("sessionId: {}, 【数据传输客户端异常】，result：{}，exception：{}", sentence, e);
            }

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
       logger.error("当前音频异常，异常信息：{}",throwable.getMessage());
    }

    @Override
    public void onCompleted() {
      logger.info("当前解析 sid {}的音频信息解析已完成",sid);
    }
}
