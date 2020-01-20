package com.iflytek.tps.service.request;

import com.iflytek.tps.foun.dto.IRequest;

public class CallBackRequest implements IRequest {

    private String sid;
    private String result;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public void verify() {

    }
}
