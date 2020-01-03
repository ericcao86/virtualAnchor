package com.iflytek.tps.foun.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.iflytek.tps.foun.util.CollectionUtils;
import com.iflytek.tps.foun.util.DateUtils;

import com.iflytek.tps.foun.util.DesAlgorithm;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.UUID;

/**
 * Created by losyn on 5/14/17.
 */
public class VerifyImage {
    public static final String IMG_EXT = "png";

    @JSONField(ordinal = 0)
    public String imageKey;

    @JSONField(ordinal = 1)
    public String extension;

    @JSONField(ordinal = 2)
    public Long time;

    @JSONField(ordinal = 3)
    public Object image;

    public VerifyImage() {
    }

    private VerifyImage(DesAlgorithm desAlgorithm, String extension, Object image) {
        this.extension = extension;
        this.image = image;
        this.time = DateUtils.time();
        this.imageKey = desAlgorithm.encrypt(Joiner.on(":").join(UUID.randomUUID(), String.valueOf(time), extension));
    }

    /** 生成 image fileKey **/
    public static VerifyImage create(DesAlgorithm desAlgorithm, String extension, Object image){
        return new VerifyImage(desAlgorithm, extension, "data:image/png;base64," + image);
    }

    /** 校验 image fileKey **/
    public static boolean verify(String identify, String imageKey){
        if(StringUtils.isBlank(identify)){
            return false;
        }
        DesAlgorithm desAlgorithm = DesAlgorithm.create(identify + VerifyImage.IMG_EXT);
        try {
            String des = desAlgorithm.decrypt(imageKey);
            List<String> desList = Splitter.on(":").splitToList(des);
            return !CollectionUtils.isNullOrEmpty(desList)
                    && 3 ==desList.size()
                    && IMG_EXT.equals(desList.get(2))
                    && verifyTime(desList.get(1));
        }catch (Exception e){
            return false;
        }
    }

    /** 校验 image fileKey 时间五分钟过期 **/
    private static boolean verifyTime(String time) {
        long t = Long.parseLong(time);
        long d = DateUtils.time() - Long.parseLong(time);
        return t > 0 && d > 0 && d < 5 * 60 * 1000L;
    }
}
