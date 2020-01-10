package com.iflytek.tps.beans.transfer;

/**
 * 实时引擎返回结果
 */
public class IstSessionResult {

    /**
     * 错误码
     */
    private int errCode;

    /**
     * 错误描述
     */
    private String errStr;

    /**
     * 结果
     */
    private String ansStr;

    /**
     * 识别结束标记
     */
    private boolean endFlag;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrStr() {
        return errStr;
    }

    public void setErrStr(String errStr) {
        this.errStr = errStr;
    }

    public String getAnsStr() {
        return ansStr;
    }

    public void setAnsStr(String ansStr) {
        this.ansStr = ansStr;
    }

    public boolean isEndFlag() {
        return endFlag;
    }

    public void setEndFlag(boolean endFlag) {
        this.endFlag = endFlag;
    }
}
