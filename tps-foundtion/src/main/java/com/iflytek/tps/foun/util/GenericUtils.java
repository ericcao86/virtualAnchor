package com.iflytek.tps.foun.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class GenericUtils {
    private GenericUtils() {
    }

    public static Class getSuperClassGenericType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (genType == null) {
            for (Type type : clazz.getGenericInterfaces()) {
                if (type != null) {
                    genType = type;
                }
            }
        }
        return getSuperClassGenericType(genType, index);
    }

    public static Class<?> getSuperClassGenericType(Type genType, int index) {
        // 如果没有实现ParameterizedType接口，即不支持泛型，直接返回Object.class
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        // 返回表示此类型实际类型参数的Type对象的数组,数组里放的都是对应类型的Class
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            throw new RuntimeException("你输入的索引" + (index < 0 ? "不能小于0" : "超出了参数的总数"));
        }
        Type param = params[index];
        if (!(param instanceof Class)) {
            if (param instanceof ParameterizedType) {
                Type rawType = ((ParameterizedType) param).getRawType();
                if (rawType instanceof Class) {
                    return (Class<?>) rawType;
                }
            }
            return Object.class;
        }
        return (Class<?>) param;
    }

    public static Class<?> getSuperClassGenericType(Type genType) {
        return getSuperClassGenericType(genType, 0);
    }

    public static Class getSuperClassGenericType(Class clazz) {
        return getSuperClassGenericType(clazz, 0);
    }

}