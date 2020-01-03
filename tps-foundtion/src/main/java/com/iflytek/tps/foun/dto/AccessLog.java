package com.iflytek.tps.foun.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.iflytek.tps.foun.util.JsonUtils;

import java.util.Map;

/**
 * Created by losyn on 5/6/17.
 */
public class AccessLog {
    @JSONField(ordinal = 0)
    public String requestTime;

    @JSONField(ordinal = 1)
    public String domain;

    @JSONField(ordinal = 2)
    public String uri;

    @JSONField(ordinal = 3)
    public String method;

    @JSONField(ordinal = 4)
    public String protocol;

    @JSONField(ordinal = 5)
    public Map<String, String> requestHeaders;

    @JSONField(ordinal = 6)
    public String params;

    @JSONField(ordinal = 7)
    public Object body;

    @JSONField(ordinal = 8)
    public String clientIp;

    @JSONField(ordinal = 9)
    public String serverIp;

    @JSONField(ordinal = 10)
    public Map<String, String> responseHeaders;

    @JSONField(ordinal = 11)
    public Object response;

    @JSONField(ordinal = 12)
    public String responseTime;

    @JSONField(serialize = false)
    public byte[] bodyBuf;
    @JSONField(serialize = false)
    public byte[] responseBuf;

    @Override
    public String toString() {
        this.body = null == bodyBuf ? null : new String(bodyBuf);
        this.response = null == responseBuf ? null : new String(responseBuf);
        return JsonUtils.toJSONString(this);
    }
}
