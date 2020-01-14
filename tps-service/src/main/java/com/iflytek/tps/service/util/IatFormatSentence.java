package com.iflytek.tps.service.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class IatFormatSentence {
    /**
     * 转写结果格式化
     * @param json  原始报文
     * @return 输出格式化后的文本
     */
    public static String formatSentence(String json) {
        if (json.length()==0)
            return "";
        StringBuilder resultBuff = new StringBuilder();
        try {
            JSONObject rt = JSON.parseObject(json);
            if (rt==null)
                return "";
            JSONArray wsArray = rt.getJSONArray("ws");
            if(wsArray != null && wsArray.size() != 0){
                for (Object ws : wsArray) {
                    com.alibaba.fastjson.JSONArray cwArray = ((com.alibaba.fastjson.JSONObject) ws).getJSONArray("cw");
                    for (Object cw : cwArray) {
                        String w = ((com.alibaba.fastjson.JSONObject) cw).getString("w");
                        resultBuff.append(w);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
           // resultBuff.append("exp: ").append(e.getMessage());
        }
        return resultBuff.toString();
    }
}
