package com.iflytek.tps.foun.dto;

public class AppException extends RuntimeException {
    public final IMessageCode code;
    public Throwable cause;
    public Object data;

    public AppException(IMessageCode code) {
        super(code.message());
        this.code = code;
    }

    public AppException(IMessageCode code, Throwable cause) {
        super(code.message(), cause);
        this.code = code;
        this.cause = cause;
    }

    public AppException(IMessageCode code, Object data) {
        super(code.message());
        this.code = code;
        this.data = data;
    }

}