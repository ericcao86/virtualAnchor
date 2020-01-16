package com.iflytek.tps.beans.transfer;

/**
 * 实时引擎入参
 */
public class IstSessionParam {

    /**
     * 会话id,这个参数一定要传,方便以后检索日志
     */
    private String sid;

    /**
     * raw(pcm音频), speex, speex-wb, opus 音频编码格式，未传入默认 speex-wb
     */
    private String aue = "raw";

    /**
     * json,plain 返回结果的格式,未传入默认json(含有时间等参数)，如果是plain则是结果字符串
     */
    private String rst = "json";

    /**
     * utf8,gbk 转写结果的编码,未传入默认utf8
     */
    private String rse = "utf8";

    /**
     * [0~600000] 引擎参数,vad后断点值, 整数毫秒,取值范围[0-60000]（暂时无用，做保留参数）
     */
    private String eos = "600000";

    /**
     * wpgs 引擎参数,是否获取中间结果
     */
    private String dwa = "wpgs";

    /**
     * 8k,16k 采样率,如果传入的不是16k,则按照16k重采样
     */
    private String rate = "16k";

    /**
     * word1;word2;... 采用;分割的热词,为utf-8编码
     */
    private String hotword = "";

    /**
     * 音频数据信息，扩展参数，保留
     */
    private String samplesInfo = "";

    public IstSessionParam() {
    }

    public IstSessionParam(String sid) {
        this.sid = sid;
    }

    public IstSessionParam(String sid, String aue, String rst, String rse, String eos, String dwa, String rate, String hotword, String samplesInfo) {
        this.sid = sid;
        this.aue = aue;
        this.rst = rst;
        this.rse = rse;
        this.eos = eos;
        this.dwa = dwa;
        this.rate = rate;
        this.hotword = hotword;
        this.samplesInfo = samplesInfo;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getAue() {
        return aue;
    }

    public void setAue(String aue) {
        this.aue = aue;
    }

    public String getRst() {
        return rst;
    }

    public void setRst(String rst) {
        this.rst = rst;
    }

    public String getRse() {
        return rse;
    }

    public void setRse(String rse) {
        this.rse = rse;
    }

    public String getEos() {
        return eos;
    }

    public void setEos(String eos) {
        this.eos = eos;
    }

    public String getDwa() {
        return dwa;
    }

    public void setDwa(String dwa) {
        this.dwa = dwa;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getHotword() {
        return hotword;
    }

    public void setHotword(String hotword) {
        this.hotword = hotword;
    }

    public String getSamplesInfo() {
        return samplesInfo;
    }

    public void setSamplesInfo(String samplesInfo) {
        this.samplesInfo = samplesInfo;
    }


}
