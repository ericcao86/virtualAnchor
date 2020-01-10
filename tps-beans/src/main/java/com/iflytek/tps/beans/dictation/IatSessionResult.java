package com.iflytek.tps.beans.dictation;

/**
 * 听写引擎返回结果
 */
public class IatSessionResult {

    /**
     * 错误描述
     */
    private String errStr;

    /**
     * 错误码
     */
    private int errCode;

    /**
     * 结果
     */
    private String ansStr;

    /**
     * 识别结束标记
     */
    private boolean endFlag;

    /**
     * @return the errStr
     */
    public String getErrStr() {
        return errStr;
    }

    /**
     * @param errStr the errStr to set
     */
    public void setErrStr(String errStr) {
        this.errStr = errStr;
    }

    /**
     * @return the errCode
     */
    public int getErrCode() {
        return errCode;
    }

    /**
     * @param errCode the errCode to set
     */
    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    /**
     * @return the ansStr
     */
    public String getAnsStr() {
        return ansStr;
    }

    /**
     * @param ansStr the ansStr to set
     */
    public void setAnsStr(String ansStr) {
        this.ansStr = ansStr;
    }

    /**
     * @return the endFlag
     */
    public boolean isEndFlag() {
        return endFlag;
    }

    /**
     * @param endFlag the endFlag to set
     */
    public void setEndFlag(boolean endFlag) {
        this.endFlag = endFlag;
    }
}
