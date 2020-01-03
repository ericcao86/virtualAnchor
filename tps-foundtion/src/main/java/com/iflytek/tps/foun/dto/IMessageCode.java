package com.iflytek.tps.foun.dto;

/**
 * Created by losyn on 6/11/17.
 */
public interface IMessageCode {
    String code();

    String msg();

    default String message(){
        return code() + " -> " + msg();
    }
}
