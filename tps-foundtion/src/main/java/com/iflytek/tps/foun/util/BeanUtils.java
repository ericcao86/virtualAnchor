package com.iflytek.tps.foun.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iflytek.tps.foun.dto.DateFormat;
import com.iflytek.tps.foun.dto.IEnum;
import com.rits.cloning.Cloner;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

public final class BeanUtils {
    private static final Logger LOG = LoggerFactory.getLogger(BeanUtils.class);

    private static final ConcurrentMap<Class<?>, ConcurrentMap<String, Field>> cfMap = Maps.newConcurrentMap();
    private static ModelMapper modelMapper = new ModelMapper();
    private static Cloner cloner = new Cloner();

    private BeanUtils() {
    }

    static {
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                // Source 不为空且类型一致的情况才做数据拷贝
                .setPropertyCondition(ctx -> ctx.getSource() != null
                        && !StringUtils.isBlank(ctx.getSource().toString())
                        && ctx.getSourceType().equals(ctx.getDestinationType()))
                .setProvider(request -> {
                    if (request.getSource() != null) {
                        Class<?> sourceClass = request.getSource().getClass();
                        Class<Object> requestedType = request.getRequestedType();
                        if (sourceClass.equals(requestedType) && requireClone(sourceClass)) {
                            return cloner.deepClone(request.getSource());
                        }
                    }
                    return null;
                });
    }

    /** 对象拷贝 **/
    public static void copy(Object source, Object target) {
        if (null != source && null != target) {
            modelMapper.map(source, target);
        }
    }

    /** 对象克隆 **/
    public static <Source, Target> Target castTo(Source source, Class<Target> destType) {
        if (null == source || null == destType) {
            return null;
        }
        try {
            Target target = destType.newInstance();
            copy(source, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("error to copy source as " + destType.getName(), e);
        }
    }

    /** 集合对象克隆 **/
    public static <Source, Target> List<Target> castTo(Collection<Source> source, Class<Target> targetType) {
        List<Target> result = Lists.newArrayListWithExpectedSize(source.size());
        for (Source src : source) {
            result.add(castTo(src, targetType));
        }
        return result;
    }

    /** Map 转 Bean **/
    public static <T> T map2Bean(Map<String, Object> source, Class<T> clazz) {
        return JsonUtils.parseObject(new JSONObject(source).toString(), clazz);
    }

    /** Bean 转 Map **/
    public static Map<String, Object> bean2Map(Object source) {
        return JsonUtils.toJSON(source);
    }

    /** 对象转 MultiValueMap **/
    public static MultiValueMap<String, Object> toMultiMap(Object source) {
        MultiValueMap<String, Object> mvm = new LinkedMultiValueMap<>();
        mvm.setAll(bean2Map(source));
        return mvm;
    }

    /** 设置对象属性值 **/
    public static void setProperty(Object bean, String name, Object value) {
        Field field = FieldUtils.getField(bean.getClass(), name, true);
        try {
            if(null == value){
                return;
            }
            Class<?> fClazz = field.getType();
            Class<?> vClazz = value.getClass();
            if(fClazz.equals(vClazz) || fClazz.isAssignableFrom(vClazz) ) {
                field.set(bean, value);
            }else {
                if(isPrimitiveType(vClazz) && isPrimitiveType(fClazz)){
                    field.set(bean, JsonUtils.parseObject(JsonUtils.toJSONBytes(value), fClazz));
                }else {
                    LOG.warn("value type {} can not cast to field type {}", vClazz, fClazz);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 取对象字段值 **/
    public static Object getProperty(Object bean, String name) {
        try {
            Field field = FieldUtils.getField(bean.getClass(), name, true);
            return field.get(bean);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 查找对象中的字段 **/
    public static Field findField(Class<?> clz, String fieldName) {
        ConcurrentMap<String, Field> fcMap = cfMap.get(clz);
        if (CollectionUtils.isNullOrEmpty(fcMap)) {
            fcMap = Maps.newConcurrentMap();
            return findField(clz, fieldName, fcMap);

        } else {
            Field field = fcMap.get(fieldName);
            if (null != field) {
                return field;
            }
            return findField(clz, fieldName, fcMap);
        }
    }

    /** 判断是否基本类型 **/
    public static boolean isPrimitiveType(Class<?> clz) {
        return clz.isPrimitive()
                || isSubTypeOf(clz, Number.class, Boolean.class, Character.class, String.class, Date.class, LocalDate.class, LocalDateTime.class);
    }

    public static boolean isSubTypeOf(Class targetClz, Class... parentClz) {
        for (Class c : parentClz) {
            if (c.isAssignableFrom(targetClz)) {
                return true;
            }
        }
        return false;
    }

    public static Object convertTypeValue(String value, Class clazz, String dateFormat) {
        if (Boolean.class.equals(clazz)) {
            return Boolean.valueOf(value);
        } else if (ClassUtils.primitiveToWrapper(clazz).equals(Character.class)) {
            return value.charAt(0);
        } else if (Integer.class.isAssignableFrom(ClassUtils.primitiveToWrapper(clazz))) {
            return value.indexOf(".") != -1 ? Double.valueOf(value).intValue() : Integer.parseInt(value);
        } else if (Long.class.isAssignableFrom(ClassUtils.primitiveToWrapper(clazz))) {
            return value.indexOf(".") != -1 ? Double.valueOf(value).longValue() : Long.parseLong(value);
        } else if (Double.class.isAssignableFrom(ClassUtils.primitiveToWrapper(clazz))) {
            return Double.valueOf(value);
        } else if (String.class.equals(clazz)) {
            return value.trim();
        } else if (clazz.equals(BigDecimal.class)) {
            return new BigDecimal(value);
        } else if (Date.class.isAssignableFrom(clazz)) {
            return DateUtils.ofDate(value, EnumUtils.realVal(dateFormat, DateFormat.class));
        } else if (IEnum.class.isAssignableFrom(clazz)) {
            return EnumUtils.realVal(value, clazz);
        } else if (Enum.class.isAssignableFrom(clazz)) {
            return Enum.valueOf(clazz, value);
        } else {
            throw new RuntimeException("unknown data type: " + clazz);
        }
    }

    public static <T> Map<String, Object> diffProperty(T oldObject, T newObject) {
        List<Field> fields = FieldUtils.getAllFieldsList(oldObject.getClass());

        Map<String, Object> res = Maps.newHashMap();
        try {
            for (final Field field : fields) {
                Object property1 = field.get(oldObject);
                Object property2 = field.get(newObject);
                if (!Objects.equals(property1, property2)) {
                    res.put(field.getName(), property2);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("diff property error.....", e);
        }
        return res;
    }

    private static Field findField(Class<?> clz, String fieldName, ConcurrentMap<String, Field> fcMap) {
        Field field = null;
        try {
            field = clz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            if (LOG.isTraceEnabled()) {
                LOG.warn("class: {} covert field {} error.....", clz.getName(), fieldName, e);
            }
            if (clz.getSuperclass() != null) {
                field = findField(clz.getSuperclass(), fieldName);
            }
        }
        if (null != field) {
            fcMap.put(fieldName, field);
            cfMap.put(clz, fcMap);
        }
        return field;
    }

    private static boolean requireClone(Class<?> type) {
        return !isPrimitiveType(type);
    }
}