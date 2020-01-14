package com.iflytek.tps.service.impl;

import com.iflytek.tps.beans.transfer.IstSessionResponse;
import com.iflytek.tps.beans.transfer.IstSessionResult;
import com.iflytek.tps.foun.dto.HttpClientResult;
import com.iflytek.tps.foun.util.HttpClientUtils;
import com.iflytek.tps.service.util.IstFormatSentence;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class IstSessionResponseImpl implements IstSessionResponse {

    private static Logger logger = LoggerFactory.getLogger(IstSessionResponseImpl.class);

    private String sid; //音频id
    private Integer idx; //音频顺序
    private String callBackUrl;//回调地址

    public IstSessionResponseImpl(String sid,Integer idx,String callBackUrl){
        this.sid = sid;
        this.idx = idx;
        this.callBackUrl = callBackUrl;
    }


    @Override
    public void onCallback(IstSessionResult istSessionResult) {
        logger.info("当前解析 sid: {}，idx :{} 的音频信息",sid,idx);
        logger.info("code:{}", istSessionResult.getErrCode());
        logger.info("str:{}", istSessionResult.getAnsStr());
        logger.info("flag:{}", istSessionResult.isEndFlag());
        String sentence = IstFormatSentence.formatSentence(istSessionResult.getAnsStr());
        logger.info("sentence:{}",sentence);
        if(StringUtils.isNoneEmpty(sentence)){
           String sidx =sid;
           String res = sentence;
            HttpClientResult result = doPost(sidx,res);//第一次发送
            if(result.getCode() != 200){
                HttpClientResult result2 = doPost(sidx,res);//第二次发送
                if(result2.getCode() != 200){
                    HttpClientResult result3 = doPost(sidx,res);//第三次发送，三次发送后无论结果如何，都不在发送
                }
            }
        }

    }

    private HttpClientResult doPost(String sid, String result){
        Map<String, String> params = new HashMap<>();
        params.put("sid",sid);
        params.put("result",result);
        HttpClientResult httpClientResult =null;
        try {
            httpClientResult = HttpClientUtils.doPost(callBackUrl,params);
        }catch (Exception e){
            logger.error("调用回调接口错误，错误message{}",e.getMessage());
        }
        return httpClientResult;
    }

    @Override
    public void onError(Throwable throwable) {
        logger.error("当前sid {},第{}段音频异常，异常信息：{}",sid,idx,throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        logger.info("当前解析 sid {}，idx {} 的音频信息解析已完成",sid,idx);
    }
}
