package com.iflytek.tps.beans.dictation;

/**
 * 听写引擎response
 */
public interface IatSessionResponse {

    /**
     * server 返回结果回调方法
     *
     * @param iatSessionResult
     */
    public void onCallback(IatSessionResult iatSessionResult);

    /**
     * server 返回出错回调方法
     *
     * @param throwable
     */
    public void onError(Throwable throwable);

    /**
     * server 完成回调方法
     */
    public void onCompleted();
}
