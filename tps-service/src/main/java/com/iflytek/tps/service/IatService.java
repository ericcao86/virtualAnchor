package com.iflytek.tps.service;
/**
 * 听写引擎service
 */

import com.iflytek.tps.beans.common.Commons;
import com.iflytek.tps.beans.dictation.IatSessionParam;
import com.iflytek.tps.beans.dictation.IatSessionResponse;

import com.iflytek.tps.foun.util.ByteUtils;
import com.iflytek.tps.service.client.IatClient;
import com.iflytek.tps.service.impl.IatSessionResponseImpl;
import com.iflytek.tps.service.request.RequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class IatService {

    private Logger logger = LoggerFactory.getLogger(IatService.class);

    @Value("${iat.url}")
    private String iatUrl;

    @Value("${callback.url}")
    private String callBackUrl;

    public Map<String,String> doConvert(RequestDto requestDto){
        Map<String,String> resMap = new HashMap<>();
        resMap.put(Commons.FLAG,Commons.SUCEESS_FLAG);
        String rate = requestDto.getSampleRate() == 8 ? "8k" : "16k";
        IatSessionParam sessionParam = new IatSessionParam(requestDto.getSid(),rate);//创建参数
        IatClient client = new IatClient(iatUrl,sessionParam);
        IatSessionResponse iatSessionResponse = new IatSessionResponseImpl(requestDto.getSid(),requestDto.getIdx(),requestDto.getIsLast(),callBackUrl);
        boolean ret = client.connect(iatSessionResponse);
        if(!ret){
            logger.error("【连接异常】sid : {},idx : {}", requestDto.getSid(),requestDto.getIdx());
            resMap.put(Commons.FLAG,Commons.ERROR_FLAG);
            return resMap;
        }
        try {
            byte [] bytes = ByteUtils.utf8Bytes(requestDto.getFrame());
            client.post(bytes);
            client.end();
            logger.info("sid"+requestDto.getSid()+" idx:"+requestDto.getIdx() + "：音频数据发送完毕！等待结果返回...");
        }catch (Exception e){
           logger.error(e.getMessage(),e);
        }

        return resMap;
    }

}
