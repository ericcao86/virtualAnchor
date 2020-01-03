package com.iflytek.tps.foun.util;

import com.iflytek.tps.foun.dto.DateFormat;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public final class DateUtils {

    private DateUtils() {
    }

    /**
     * 给定 format 将 Date 转换成 string
     **/
    public static String format(Date date, DateFormat format) {
        return null == date ? format.val() : new SimpleDateFormat(format.val()).format(date);
    }

    /**
     * 给定 format 将 LocalDate 转换成 string
     **/
    public static String format(LocalDate date, DateFormat format) {
        return null == date ? format.val() : date.format(DateTimeFormatter.ofPattern(format.val()));
    }

    /**
     * 给定 format 将 LocalDateTime 转换成 string
     **/
    public static String format(LocalDateTime date, DateFormat format) {
        return null == date ? format.val() : date.format(DateTimeFormatter.ofPattern(format.val()));
    }

    /**
     * 给定 format 将秒时间戳转换成 string
     **/
    public static String format(long unixTime, DateFormat format) {
        return format(ofLocalDateTime(unixTime), format);
    }

    /**
     * 给定 format 将 string 转换成 Date
     **/
    public static Date ofDate(String sDate, DateFormat format) {
        try {
            return new SimpleDateFormat(format.val()).parse(sDate);
        } catch (Exception e) {
            throw new RuntimeException("ofDate error, sDate: " + sDate + " format: " + format.val(), e);
        }
    }

    /**
     * 将秒时间戳 转换成 Date
     **/
    public static Date ofDate(long time) {
        return Date.from(Instant.ofEpochMilli(time));
    }

    /**
     * 给定 format 将 string 转换成 LocalDate
     **/
    public static LocalDate ofLocalDate(String sDate, DateFormat format) {
        return LocalDate.parse(sDate, DateTimeFormatter.ofPattern(format.val()));
    }

    /**
     * 将毫秒时间戳 转换成 LocalDate
     **/
    public static LocalDate date2Local(long date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 将秒时间戳 转换成 LocalDate
     **/
    public static LocalDate ofLocalDate(long time) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 将毫秒时间戳 转换成 LocalDateTime
     **/
    public static LocalDateTime time2Local(long time) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }

    /**
     * 给定 format 将 string 转换成 LocalDateTime
     **/
    public static LocalDateTime ofLocalDateTime(String sDate, DateFormat format) {
        return LocalDateTime.parse(sDate, DateTimeFormatter.ofPattern(format.val()));
    }

    /**
     * 将秒时间戳 转换成 LocalDateTime
     **/
    public static LocalDateTime ofLocalDateTime(long time) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }

    /**
     * 当前系统时间 Date 类型
     **/
    public static Date now() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 给定 format 将当前系统时间转换成 string
     **/
    public static String now(DateFormat format) {
        return format(now(), format);
    }

    /**
     * 当前系统时间秒
     **/
    public static Long second() {
        return Instant.now().getEpochSecond();
    }

    /**
     * Date 时间秒
     **/
    public static Long second(Date date) {
        return null == date ? 0L : date.getTime() / 1000;
    }

    /**
     * LocalDate 时间秒
     **/
    public static Long second(LocalDate date) {
        return null == date ? 0L : date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    /**
     *  LocalDateTime 时间秒
     **/
    public static Long second(LocalDateTime dateTime) {
        return null == dateTime ? 0L : dateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    /**
     * 当前系统时间毫秒
     **/
    public static Long time() {
        return System.currentTimeMillis();
    }

    /**
     * 将 LocalDate 转换成毫秒
     **/
    public static Long time(Date date) {
        return null == date ? 0L : date.getTime();
    }

    /**
     * 将 LocalDate 转换成毫秒
     **/
    public static Long time(LocalDate date) {
        return null == date ? 0L : date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 将 LocalDateTime 转换成毫秒
     **/
    public static Long time(LocalDateTime dateTime) {
        return null == dateTime ? 0L : dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 给定秒时间戳得出当天的开始时间秒
     **/
    public static long ofDayStart(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ofDate(time));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.toInstant().toEpochMilli();
    }

    /**
     * 在给定的 Date 时间加 days 天
     **/
    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * from 1 (Monday) to 7 (Sunday)
     **/
    public static int dayOfWeek() {
        return LocalDate.now().getDayOfWeek().getValue();
    }

    public static int dayOfWeek(LocalDate date) {
        return null == date ? 0 : date.getDayOfWeek().getValue();
    }

    /** 给定时间为一周的第几天，from 1 (Monday) to 7 (Sunday)， 0 InvalidDate **/
    public static int weekOfDate(Date date){
        return null == date ? 0 : ofLocalDate(date.getTime()).getDayOfWeek().getValue();
    }

    /** 获取两个日期之前相差的天数据 */
    public static int daySize(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return -1;
        }
        long timMillis = ofDayStart(endDate.getTime()) - ofDayStart(startDate.getTime());
        return BigDecimal.valueOf(timMillis)
                .divide(BigDecimal.valueOf(24 * 60 * 60 * 1000L), 0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    public static int monthDays(int year, int month){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.get(Calendar.DATE);
    }
}
