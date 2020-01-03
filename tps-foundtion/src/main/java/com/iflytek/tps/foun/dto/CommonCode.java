package com.iflytek.tps.foun.dto;

/**
 * Created by losyn on 6/11/17.
 */
public enum CommonCode implements IMessageCode{
    Ok("200", "成功"),

    Error("500", "服务异常"),

    Forbidden("403", "服务没有权限访问"),

    NotFound("404", "服务未找到"),

    Timeout("12002", "服务处理超时");

    private final String code;
    private final String desc;
    CommonCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override public String code() {
        return code;
    }

    @Override public String msg() {
        return desc;
    }
}
