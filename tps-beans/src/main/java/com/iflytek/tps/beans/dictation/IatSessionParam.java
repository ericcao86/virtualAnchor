package com.iflytek.tps.beans.dictation;

/**
 * 听写引擎入参
 */
public class IatSessionParam {

    /**
     * 会话id,这个参数一定要传,方便以后检索日志
     */
    private String sid;

    /**
     * raw(pcm音频),speex,speex-wb,opus,音频编码格式,未传入默认speex-web
     */
    private String aue = "raw";

    /**
     * json,plain 返回结果的格式,未传入默认json
     */
    private String rst = "json";

    /**
     * utf8,gbk 转写结果的编码,未传入默认utf8
     */
    private String rse = "utf8";

    /**
     * [0~600000] 引擎参数,vad后断点值,整数,毫秒,取值范围[0-60000]
     */
    private String eos;


    /**
     * wpgs 引擎参数,是否获取中间结果
     */
    private String dwa;

    /**
     * [0~5] 引擎参数,多候选（句子级）
     */
    private String wbest;

    /**
     * true,false 引擎参数,是否只有人名多候选
     */
    private String wbestonlyper;

    /**
     * 8k,16k 采样率,如果传入的不是16k,则按照16k重采样
     */
    private String rate;

    /**
     * word1;word2;... 采用;分割的热词，为utf-8编码
     */
    private String hotword;

    /**
     * 引擎保留字段,默认传 ""
     */
    private String samplesInfo = "";

    /**
     * IatSessionParamw无参构造方法
     */
    public IatSessionParam() {

    }

    /**
     * IatSessionParam最简构造方法
     *
     * @param sid  会话id，必传
     * @param rate 音频采样率，必传
     */
    public IatSessionParam(String sid, String rate) {
        this.sid = sid;
        this.rate = rate;
    }

    public IatSessionParam(String sid, String aue, String rst, String rse, String eos, String dwa, String wbest, String wbestonlyper, String rate, String hotword, String samplesInfo) {
        this.sid = sid;
        this.aue = aue;
        this.rst = rst;
        this.rse = rse;
        this.eos = eos;
        this.dwa = dwa;
        this.wbest = wbest;
        this.wbestonlyper = wbestonlyper;
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

    public String getWbest() {
        return wbest;
    }

    public void setWbest(String wbest) {
        this.wbest = wbest;
    }

    public String getWbestonlyper() {
        return wbestonlyper;
    }

    public void setWbestonlyper(String wbestonlyper) {
        this.wbestonlyper = wbestonlyper;
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

    public String getSamplesInfo() {
        return samplesInfo;
    }

    public void setSamplesInfo(String samplesInfo) {
        this.samplesInfo = samplesInfo;
    }

    @Override
    public String toString() {
        return "IatSessionParam{" +
                "sid='" + sid + '\'' +
                ", aue='" + aue + '\'' +
                ", rst='" + rst + '\'' +
                ", rse='" + rse + '\'' +
                ", eos='" + eos + '\'' +
                ", dwa='" + dwa + '\'' +
                ", wbest='" + wbest + '\'' +
                ", wbestonlyper='" + wbestonlyper + '\'' +
                ", rate='" + rate + '\'' +
                ", hotword='" + hotword + '\'' +
                ", samplesInfo='" + samplesInfo + '\'' +
                '}';
    }
}
