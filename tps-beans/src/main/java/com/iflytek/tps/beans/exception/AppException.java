package com.iflytek.tps.beans.exception;

import java.io.Serializable;

public class AppException extends Exception implements Serializable {
    private static final long serialVersionUID = 5018770229232677878L;

    private int errorCode = 1000;

    public AppException() {
    }

    public AppException(int errorCode, String arg0) {
        super(arg0);
        this.errorCode = errorCode;
    }

    public AppException(String arg0) {
        super(arg0);
    }

    public AppException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public AppException(int errorCode, String arg0, Throwable arg1) {
        super(arg0, arg1);
        this.errorCode = errorCode;
    }

    public AppException(Throwable arg0) {
        super(arg0);
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}
