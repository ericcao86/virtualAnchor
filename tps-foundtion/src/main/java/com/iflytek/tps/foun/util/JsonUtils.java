package com.iflytek.tps.foun.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class JsonUtils {

    private JsonUtils() {
    }

    public static String toJSONString(Object o, SerializerFeature... features) {
        SerializerFeature[] sfArray = CollectionUtils.isNullOrEmpty(features) ? new SerializerFeature[]{
                SerializerFeature.QuoteFieldNames,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteDateUseDateFormat
        } : features;
        return JSON.toJSONString(o, new SerializeFilter[0], sfArray);
    }

    public static String toJSONString(Object o, SerializeFilter filter, SerializerFeature... features) {
        return JSON.toJSONString(o, filter, features);
    }

    public static <T> T parseObject(byte[] body, Class<T> clazz) {
        return null == body ? null : JSON.parseObject(body, clazz);
    }

    public static <T> T parseObject(String body, Class<T> clazz) {
        return JSON.parseObject(body, clazz);
    }

    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz);
    }

    public static <T> byte[] toJSONBytes(T data) {
        return JSON.toJSONBytes(data);
    }

    public static JSONObject toJSON(Object source) {
        return (JSONObject) JSON.toJSON(source);
    }

    public static JSONObject parseObject(String jsonString) {
        return JSON.parseObject(jsonString);
    }

    public static <T> T parseByFilterKey(String jsonString, Class<T> clazz, Function<String, String> keyFilter) {
        JSONObject result = new JSONObject();
        deepCopyJsonObject(JSON.parseObject(jsonString), result, keyFilter);
        return parseObject(result.toJSONString(), clazz);
    }

    public static JSONObject fromStream(InputStream in) {
        return fromStream(in, null, JSONObject.class);
    }

    public static <T> T fromStream(InputStream in, Class<T> clazz) {
        return fromStream(in, null, clazz);
    }

    public static JSONObject fromStream(InputStream in, String name) {
        return fromStream(in, name, JSONObject.class);
    }

    public static <T> T fromStream(InputStream in, String name, Class<T> clazz) {
        try {
            StringWriter sw = new StringWriter();
            IOUtils.copy(in, sw, "UTF-8");
            if (StringUtils.isBlank(name)) {
                return JsonUtils.parseObject(sw.toString(), clazz);
            }
            JSONObject jsonObj = JSON.parseObject(sw.toString());
            if (!jsonObj.containsKey(name)) {
                return null;
            }
            return jsonObj.getObject(name, clazz);
        } catch (Exception e) {
            throw new RuntimeException("parse object from input stream error.....", e);
        }finally {
            IOUtils.closeQuietly(in);
        }
    }

    private static void deepCopyJsonObject(JSONObject source, JSONObject target, Function<String, String> keyFilter) {
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            Object value = entry.getValue();
            String newKey = keyFilter.apply(entry.getKey());
            if (value instanceof JSONObject) {
                JSONObject newChild = new JSONObject();
                deepCopyJsonObject((JSONObject) value, newChild, keyFilter);
                target.put(newKey, newChild);
            } else if (value instanceof JSONArray) {
                JSONArray arrayValue = (JSONArray) value;
                JSONArray newArrayValue = new JSONArray();
                deepCopyJsonArray(arrayValue, newArrayValue, keyFilter);
                target.put(newKey, newArrayValue);
            } else {
                target.put(newKey, value);
            }
        }
    }

    private static void deepCopyJsonArray(JSONArray source, JSONArray target, Function<String, String> keyFilter) {
        for (Object child : source) {
            if (child instanceof JSONArray) {
                JSONArray newChildArray = new JSONArray();
                deepCopyJsonArray((JSONArray) child, newChildArray, keyFilter);
                target.add(newChildArray);
            } else if (child instanceof JSONObject) {
                JSONObject newJsonObject = new JSONObject();
                deepCopyJsonObject((JSONObject) child, newJsonObject, keyFilter);
                target.add(newJsonObject);
            } else {
                target.add(child);
            }
        }
    }

}
