package com.iflytek.tps.foun.util;

import com.alibaba.fastjson.util.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by losyn on 7/12/17.
 */
public final class ByteUtils {
    private ByteUtils() {
    }

    public static String string(byte[] source) {
        return null == source ? StringUtils.EMPTY : new String(source, IOUtils.UTF8);
    }

    public static byte[] utf8Bytes(String info) {
        if(StringUtils.isBlank(info)){
            return StringUtils.EMPTY.getBytes(IOUtils.UTF8);
        }
        return info.getBytes(IOUtils.UTF8);
    }
}
