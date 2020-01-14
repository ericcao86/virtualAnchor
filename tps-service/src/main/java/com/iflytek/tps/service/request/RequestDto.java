package com.iflytek.tps.service.request;

import com.iflytek.tps.foun.dto.IRequest;

import javax.validation.constraints.NotNull;

public class RequestDto implements IRequest {

    @NotNull
    private String frame;//音频字节
    @NotNull
    private String sid;//音频id
    @NotNull
    private Integer idx;//音频顺序

    private Integer isLast;//是否最后一包 1是0否

    @NotNull
    private Integer sampleRate;//采样率 8为8k，16为16k


    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public Integer getIsLast() {
        return isLast;
    }

    public void setIsLast(Integer isLast) {
        this.isLast = isLast;
    }

    public Integer getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(Integer sampleRate) {
        this.sampleRate = sampleRate;
    }


    @Override
    public String toString() {
        return "RequestDto{" +
                "frame='" + frame + '\'' +
                ", sid='" + sid + '\'' +
                ", idx=" + idx +
                ", isLast=" + isLast +
                ", sampleRate=" + sampleRate +
                '}';
    }

    @Override
    public void verify() {

    }
}
