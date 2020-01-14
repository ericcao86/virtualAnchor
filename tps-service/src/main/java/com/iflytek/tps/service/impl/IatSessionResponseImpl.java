package com.iflytek.tps.service.impl;

import com.iflytek.tps.beans.dictation.IatSessionResponse;
import com.iflytek.tps.beans.dictation.IatSessionResult;
import com.iflytek.tps.foun.dto.HttpClientResult;
import com.iflytek.tps.foun.util.HttpClientUtils;
import com.iflytek.tps.service.util.IatFormatSentence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IatSessionResponseImpl implements IatSessionResponse {

    private String sid;//音频sid
    private int idx;//音频发送顺序
    private int isLast;//是否最后一包 1 是 0 否
    private int sampleRate;//采样率，8/16
    private String callBackUrl;//回调地址
    private List<String> resList = new ArrayList<>();

    public IatSessionResponseImpl(String sid,int idx,int isLast,String callBackUrl){
        this.sid = sid;
        this.idx = idx;
        this.isLast = isLast;
        this.callBackUrl =callBackUrl;
    }

    private Logger logger = LoggerFactory.getLogger(IatSessionResponseImpl.class);

    @Override
    public void onCallback(IatSessionResult iatSessionResult) {
        logger.info("当前解析 sid: {}，idx :{} 的音频信息",sid,idx);
        logger.info("code:{}", iatSessionResult.getErrCode());
        logger.info("str:{}", iatSessionResult.getAnsStr());
        logger.info("flag:{}", iatSessionResult.isEndFlag());
        String sentence = IatFormatSentence.formatSentence(iatSessionResult.getAnsStr());
        logger.info("sentence:{}",sentence);
        if(isLast == 0){//如果不是最后一包
            resList.add(sentence);
        }else{
            StringBuffer buffer = new StringBuffer();
            resList.stream().forEach(e->buffer.append(e));
            String sidres = sid;
            String resultStr = buffer.toString();
            //TODO 发送回调接口
            HttpClientResult result = doPost(sidres,resultStr);
            if(result.getCode() != 200){//如果不等于200
                HttpClientResult result1 = doPost(sidres,resultStr);//发送第二次
                if (result1.getCode() != 200){
                    HttpClientResult result2 = doPost(sidres,resultStr);//发送第三次
                    //发送三次后，无论正确与否，释放内存内容
                    resList.clear();
                }
            }
        }
    }

    private HttpClientResult doPost(String sid,String result){
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
