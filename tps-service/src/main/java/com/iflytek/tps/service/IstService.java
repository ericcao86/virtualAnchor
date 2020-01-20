package com.iflytek.tps.service;
/**
 * 实时引擎service
 */
import com.iflytek.tps.beans.common.Commons;
import com.iflytek.tps.beans.transfer.IstSessionParam;
import com.iflytek.tps.beans.transfer.IstSessionResponse;
import com.iflytek.tps.foun.util.ByteUtils;
import com.iflytek.tps.service.client.IstClient;
import com.iflytek.tps.service.impl.IstSessionResponseImpl;
import com.iflytek.tps.service.request.RequestDto;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class IstService {
    private Logger logger = LoggerFactory.getLogger(IstService.class);

    @Value("${ist.url}")
    private String istUrl;

    @Value("${callback.url}")
    private String callBackUrl;

    public Map<String,String> doConvert(RequestDto requestDto){
        Map<String,String> resMap = new HashMap<>();
        resMap.put(Commons.FLAG,Commons.SUCEESS_FLAG);
        IstSessionParam sessionParam = new IstSessionParam(requestDto.getSid());
        String rate = requestDto.getSampleRate() == 8 ? "8k" : "16K";
        sessionParam.setRate(rate);
        sessionParam.setDwa("");
        IstClient client  = new IstClient(istUrl,sessionParam);
        IstSessionResponse istSessionResponse = new IstSessionResponseImpl(requestDto.getSid(),callBackUrl);
        boolean ret = client.connect(istSessionResponse);
        if(!ret){
            logger.error("【连接异常】sid : {}", requestDto.getSid());
            resMap.put(Commons.FLAG,Commons.ERROR_FLAG);
            return resMap;
        }
        try {
            byte [] bytes = Base64.decodeBase64(requestDto.getFrame());
            client.post(bytes);
            client.end();
            logger.info("sid"+requestDto.getSid() + "：音频数据发送完毕！等待结果返回...");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }



        return resMap;
    }


}
