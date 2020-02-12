package com.iflytek.tps.service;

import com.alibaba.fastjson.JSONObject;
import com.iflytek.tps.beans.virtual.VirtualAddObject;
import com.iflytek.tps.foun.util.HttpRestUtils;
import com.iflytek.tps.service.util.CommonMap;
import com.iflytek.tps.service.util.SignUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class VirtualService {

    @Value("${create.url}")
    private String createUrl;

    @Value("${task.url}")
    private String taskUrl;

    private static  String CREATE_JSON="{\"base\":{\"appid\":\"a2te42kf\",\"sid\":\"17d82f98-d7c4-4c83-8de0-76568d172ce7\",\"timestamp\":${timestamp},\"sign\":\"${sign}\"},\"param\":{\"vcn\":\"xiaopei\",\"spd\":0,\"vol\":50,\"type\":1,\"text\":\"${text}\",\"format\":\"mp4\",\"location\":\"0\",\"anchorId\":5,\"bgType\":0,\"subtitles\":0,\"width\":\"1080\",\"height\":\"1920\",\"bgUrl\":\"https://i.loli.net/2020/02/11/6sWjgPqXYJDw3BF.png\",\"bgType\":0,\"bgAudioVol\":\"0.5\"}}";

    private static final String secret = "ye3712c7123fa7a70a139e2c4751f123";

    private static final String TASK_JSON="{\"base\":{\"appid\":\"a2te42kf\",\"sid\":\"17d82f98-d7c4-4c83-8de0-76568d172ce7\",\"timestamp\":${timestamp},\"sign\":\"${sign}\"},\"param\":{\"taskId\":\"${taskId}\"}}";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public String getVideoUrl(Map params){
        params.forEach((k,v)->{
            String js = CREATE_JSON.replace("${text}",(String)v).replace("${timestamp}",System.currentTimeMillis()+"");
            JSONObject object = JSONObject.parseObject(js);
            String sign = SignUtils.buildSign(object,secret);
            js = js.replace("${sign}",sign);
            RequestBody body = RequestBody.create(JSON, js);
            VirtualAddObject virtualAddObject = HttpRestUtils.post(createUrl,body, VirtualAddObject.class);
            String taskId = virtualAddObject.getData().getTaskId();
            if(StringUtils.isNotBlank(taskId)){
                CommonMap.getTaskMap().put(k,taskId);
            }
        });

        return "sucess";
    }
}
