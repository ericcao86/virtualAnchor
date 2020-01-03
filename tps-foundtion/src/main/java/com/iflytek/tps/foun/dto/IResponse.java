package com.iflytek.tps.foun.dto;

import java.io.Serializable;

interface IResponse<T> extends Serializable {
    Class<T> clazz();
}