package com.iflytek.tps.beans.virtual;

public class VirtualAddObject {

    private String code;
    private String message;
    private VirtualAddData data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public VirtualAddData getData() {
        return data;
    }

    public void setData(VirtualAddData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "VirtualAddObject{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
