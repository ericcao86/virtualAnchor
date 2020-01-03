package com.iflytek.tps.foun.dto;

public enum DateFormat implements IEnum<String> {
    ShortNumDate("yyMMdd"),

    NumDate("yyyyMMdd"),

    StrikeDate("yyyy-MM-dd"),

    NumDateTime("yyyyMMddHHmmss"),

    TwoYearNumDateTime("yyMMddHHmmss"),

    StrikeDateTime("yyyy-MM-dd HH:mm:ss"),

    MillisecondTime("yyyy-MM-dd HH:mm:ss SSS"),

    NumTime("HHmmss"),

    ColonTime("HH:mm:ss");

    private final String value;
    DateFormat(String value) {
        this.value = value;
    }

    @Override
    public String val() {
        return value;
    }
}