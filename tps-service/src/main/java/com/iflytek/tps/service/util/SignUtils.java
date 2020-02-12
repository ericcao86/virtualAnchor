package com.iflytek.tps.service.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SignUtils {
    private static final String SEPARATOR = "\u0026";


    /**
     * 构建签名
     *
     * @param request
     * @param secret
     * @return
     */
    public static String buildSign(JSONObject request, String secret) {
        if (request == null) {
            return null;
        }
        String signStr = buildSignStr(request) + SEPARATOR + secret;
        return DigestUtils.md5Hex(signStr);
    }

    /**
     * 构建用于签名的字符串
     *
     * @param request
     * @return
     */
    private static String buildSignStr(JSONObject request) {
        StringBuilder builder = new StringBuilder();
        List<String> keys = new LinkedList<>();
        keys.addAll(request.keySet());
        keys.remove("sign");
        Collections.sort(keys);
        for (String key : keys) {
            Object obj = request.get(key);
            if (obj instanceof JSONObject) {
                if (((JSONObject) obj).size() == 0) {
                    continue;
                }
                builder.append(key).append("=").append(buildSignStr((JSONObject) obj)).append(SEPARATOR);
            } else if (obj instanceof JSONArray) {
                StringBuilder sb = new StringBuilder();
                JSONArray array = (JSONArray) obj;
                array.forEach(json -> sb.append(json instanceof String ? json : buildSignStr((JSONObject) json)).append(","));
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                builder.append(key).append("=").append(sb.toString()).append(SEPARATOR);
            } else {
                if (obj instanceof CharSequence && StringUtils.isBlank((CharSequence) obj)) {
                    continue;
                }
                builder.append(key).append("=").append(obj).append(SEPARATOR);
            }
        }
        int lenth = builder.length();
        if (lenth > 0) {
            builder.deleteCharAt(lenth - 1);
        }
        return builder.toString();
    }

    public static void main(String ARGS[]){
        String json = "{\"base\":{\"appid\":\"a2te42kf\",\"sid\":\"17d82f98-d7c4-4c83-8de0-76568d172ce7\",\"timestamp\":1587676767676,\"sign\":\"${sign}\"},\"param\":{\"vcn\":\"xiaopei\",\"spd\":0,\"vol\":50,\"type\":1,\"text\":\"${text}\",\"format\":\"mp4\",\"location\":\"0\",\"anchorId\":5,\"bgType\":0,\"subtitles\":0,\"ratio\":\"1080\",\"bgAudioVol\":\"0.5\"}}";
        JSONObject object = JSONObject.parseObject(json);
       String sign = SignUtils.buildSign(object,"ye3712c7123fa7a70a139e2c4751f123");
       System.out.println(sign);

    }
}
