package com.iflytek.tps.foun.dto;


import com.iflytek.tps.foun.util.JsonUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.io.InputStream;

/**
 * 服务构建信息
 */
public class SrvInspectInfo {
    private static final Logger LOG = LoggerFactory.getLogger(WebMvcConfigurationSupport.class);

    private static SrvInspectInfo INFO;

    /** 服务名 **/
    public String name = StringUtils.EMPTY;

    /** 构建版本 **/
    public String version = StringUtils.EMPTY;

    /** 构建描述信息 **/
    public String desc = StringUtils.EMPTY;

    /** 构建时间 **/
    public String time = StringUtils.EMPTY;

    /** 初始化构建信息 **/
    private SrvInspectInfo() {
        InputStream is = null;
        SrvInspectInfo si = null;
        try {
            is = new ClassPathResource("release.info").getInputStream();
            si = JsonUtils.fromStream(is, SrvInspectInfo.class);
        } catch (Exception e) {
            LOG.warn("read service inspects info error: ", e);
        }finally {
            IOUtils.closeQuietly(is);
        }
        INFO = null == si ? new SrvInspectInfo() : si;
    }

    public static SrvInspectInfo info(){
        return null == INFO ? new SrvInspectInfo() : INFO;
    }
}
