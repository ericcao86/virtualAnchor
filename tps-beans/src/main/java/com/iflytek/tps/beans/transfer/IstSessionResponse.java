package com.iflytek.tps.beans.transfer;

/**
 * 实时引擎response
 */
public interface IstSessionResponse {
    /**
     * server 返回结果回调方法
     *
     * @param istSessionResult
     */
    public void onCallback(IstSessionResult istSessionResult);

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
