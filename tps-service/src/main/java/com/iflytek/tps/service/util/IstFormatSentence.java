package com.iflytek.tps.service.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class IstFormatSentence {

    public static String formatSentence(String json) {
        if (json == null || json.length() == 0)
            return "";
        StringBuilder result = new StringBuilder();
        try {

            JsonObject jsonObj = new JsonParser().parse(json).getAsJsonObject();
            if (jsonObj.has("cn")) {
                // ist
                JsonObject st = jsonObj.getAsJsonObject("cn").getAsJsonObject("st");
                String rl = st.get("rl") == null ? null : st.get("rl").getAsString();
                JsonArray rtList = st.getAsJsonArray("rt");
                for (int i = 0; i < rtList.size(); i++) {
                    if (null != rl) {
                        result.append(rl).append(":");
                    }
                    result.append(getRT(rtList.get(i).getAsJsonObject()));
                }
            } else if (jsonObj.has("ws")) {
                // iat
                result.append(getRT(jsonObj));
            } else if (jsonObj.has("lattice")) {
                // quark
                if (0 == jsonObj.getAsJsonObject("state").get("ok").getAsInt()) {
                    JsonArray latticeList = jsonObj.getAsJsonArray("lattice");
                    for (int i = 0; i < latticeList.size(); i++) {
                        result.append("\n");
                        String best = latticeList.get(i).getAsJsonObject().get("json_1best").getAsString();
                        JsonObject bestJsonObj = new JsonParser().parse(best).getAsJsonObject();
                        JsonObject st = bestJsonObj.getAsJsonObject("st");
                        String rl = st.get("rl").getAsString();
                        JsonArray rtList = st.getAsJsonArray("rt");
                        for (int j = 0; j < rtList.size(); j++) {
                            if (null != rl) {
                                result.append(rl).append(":");
                            }
                            result.append(getRT(rtList.get(j).getAsJsonObject()));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.append("exp: ").append(e.getMessage());
        }
        return result.toString();
    }

    public static String getRT(JsonObject rt) {
        StringBuilder sb = new StringBuilder();
        JsonArray wsArray = rt.getAsJsonArray("ws");
        if (wsArray != null && wsArray.size() != 0) {
            final int size = wsArray.size();
            for (int i = 0; i < size; i++) {
                JsonObject ws = wsArray.get(i).getAsJsonObject();
                JsonArray cwArray = ws.getAsJsonArray("cw");
                final int sizeCw = cwArray.size();
                for (int j = 0; j < sizeCw; j++) {
                    JsonObject cw = cwArray.get(j).getAsJsonObject();
                    String w = cw.get("w").getAsString();
                    sb.append(w);
                }
            }
        }
        return sb.toString();
    }
}
