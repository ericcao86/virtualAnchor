package com.iflytek.tps.foun.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

/**
 * geo hash 计算
 */
public final class GeoHashUtils {
    //地球半径,单位米
    private static final double EARTH_RADIUS = 6378137;
    private static final BigDecimal BD2 = new BigDecimal(2);
    private static final int SCALE = 6;
    //取 geohash 前 5 位区块，经纬度转化为二进制长度
    private static final int LAT_LENGTH = 12;
    private static final int LNG_LENGTH = 13;
    //经纬度单独编码长度
    private static int NUM_BITS = 6 * 5;
    //32位编码对应字符
    private final static char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p',
            'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    //定义编码映射关系  
    private final static HashMap<Character, Integer> LOOK_UP = Maps.newHashMap();

    //每格经纬度的单位大小
    private static final HashMap<String, BigDecimal> MIN_LAT_LNG = Maps.newHashMap();

    private static final BigDecimal BD90 = new BigDecimal("90");
    private static final BigDecimal BD_90 = new BigDecimal("-90");
    private static final BigDecimal BD180 = new BigDecimal("180");
    private static final BigDecimal BD_180 = new BigDecimal("-180");

    //初始化编码映射内容
    static {
        int i = 0;
        for (char c : DIGITS) {
            LOOK_UP.put(c, i++);
        }
        BigDecimal minLat = BD90.subtract(BD_90);
        for (i = 0; i < LAT_LENGTH; i++) {
            minLat = minLat.divide(BD2);
        }
        MIN_LAT_LNG.put("MIN_LAT", minLat);
        BigDecimal minLng = BD180.subtract(BD_180);
        for (i = 0; i < LNG_LENGTH; i++) {
            minLng = minLng.divide(BD2);
        }
        MIN_LAT_LNG.put("MIN_LNG", minLng);
    }

    private GeoHashUtils() {
    }

    /** 求所在坐标点及周围点组成的九个 */
    public static List<String> nearNine(BigDecimal lat, BigDecimal lon) {
        BigDecimal leftLat = MathUtils.nvl(lat).subtract(MIN_LAT_LNG.get("MIN_LAT"));
        BigDecimal rightLat = MathUtils.nvl(lat).add(MIN_LAT_LNG.get("MIN_LAT"));

        BigDecimal upLng = MathUtils.nvl(lon).subtract(MIN_LAT_LNG.get("MIN_LNG"));
        BigDecimal downLng = MathUtils.nvl(lon).add(MIN_LAT_LNG.get("MIN_LNG"));

        List<String> base32For9 = Lists.newArrayList();

        //左侧从上到下 3个
        String leftUp = getGeoHashBase32(leftLat, upLng);
        if (!(leftUp == null || "".equals(leftUp))) {
            base32For9.add(leftUp);
        }
        String leftMid = getGeoHashBase32(leftLat, lat);
        if (!(leftMid == null || "".equals(leftMid))) {
            base32For9.add(leftMid);
        }
        String leftDown = getGeoHashBase32(leftLat, downLng);
        if (!(leftDown == null || "".equals(leftDown))) {
            base32For9.add(leftDown);
        }

        //中间从上到下 3个
        String midUp = getGeoHashBase32(lat, upLng);
        if (!(midUp == null || "".equals(midUp))) {
            base32For9.add(midUp);
        }
        String midMid = getGeoHashBase32(lat, lon);
        if (!(midMid == null || "".equals(midMid))) {
            base32For9.add(midMid);
        }
        String midDown = getGeoHashBase32(lat, downLng);
        if (!(midDown == null || "".equals(midDown))) {
            base32For9.add(midDown);
        }

        //右侧从上到下 3个
        String rightUp = getGeoHashBase32(rightLat, upLng);
        if (!(rightUp == null || "".equals(rightUp))) {
            base32For9.add(rightUp);
        }
        String rightMid = getGeoHashBase32(rightLat, lon);
        if (!(rightMid == null || "".equals(rightMid))) {
            base32For9.add(rightMid);
        }
        String rightDown = getGeoHashBase32(rightLat, downLng);
        if (!(rightDown == null || "".equals(rightDown))) {
            base32For9.add(rightDown);
        }
        return base32For9;
    }


    /**
     * 计算地球上任意两点(经纬度)距离, 单位：米
     */
    public static BigDecimal distance(BigDecimal lonA, BigDecimal latA, BigDecimal lonB, BigDecimal latB) {
        latA = rad(latA);
        latB = rad(latB);
        BigDecimal aDiff = MathUtils.nvl(latA).subtract(latB);
        BigDecimal nDif = rad(MathUtils.nvl(lonA).subtract(lonB));

        double sa2 = Math.sin(aDiff.divide(BD2, SCALE, BigDecimal.ROUND_HALF_UP).doubleValue());
        double sb2 = Math.sin(nDif.divide(BD2, SCALE, BigDecimal.ROUND_HALF_UP).doubleValue());
        double distance = 2
                * EARTH_RADIUS
                * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(latA.doubleValue())
                * Math.cos(latB.doubleValue()) * sb2 * sb2));
        return BigDecimal.valueOf(distance);
    }

    /**
     * 对编码后的字符串解码
     */
    public static BigDecimal[] decode(String geoHash) {
        StringBuilder buffer = new StringBuilder();
        for (char c : geoHash.toCharArray()) {
            int i = LOOK_UP.get(c) + 32;
            buffer.append(Integer.toString(i, 2).substring(1));
        }
        BitSet lonSet = new BitSet();
        BitSet latSet = new BitSet();
        //偶数位，经度
        int j = 0;
        for (int i = 0; i < NUM_BITS * 2; i += 2) {
            boolean isSet = false;
            if (i < buffer.length())
                isSet = buffer.charAt(i) == '1';
            lonSet.set(j++, isSet);
        }
        //奇数位，纬度
        j = 0;
        for (int i = 1; i < NUM_BITS * 2; i += 2) {
            boolean isSet = false;
            if (i < buffer.length())
                isSet = buffer.charAt(i) == '1';
            latSet.set(j++, isSet);
        }
        BigDecimal lon = decode(lonSet, BD_180, BD180);
        BigDecimal lat = decode(latSet, BD_90, BD90);

        return new BigDecimal[]{lat, lon};
    }

    /**
     * 对经纬度进行编码
     */
    public static String encode(BigDecimal lat, BigDecimal lon) {
        BitSet latSet = getBitSet(lat, BD_90, BD90);
        BitSet lonSet = getBitSet(lon, BD_180, BD180);
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < NUM_BITS; i++) {
            buffer.append((lonSet.get(i)) ? '1' : '0');
            buffer.append((latSet.get(i)) ? '1' : '0');
        }
        return base32(Long.parseLong(buffer.toString(), 2));
    }

    private static BigDecimal rad(BigDecimal dot) {
        return MathUtils.nvl(dot)
                .multiply(BigDecimal.valueOf(Math.PI))
                .divide(BD180, SCALE, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 根据二进制和范围解码
     */
    private static BigDecimal decode(BitSet bs, BigDecimal floor, BigDecimal ceiling) {
        BigDecimal mid = BigDecimal.ZERO;
        for (int i = 0; i < bs.length(); i++) {
            mid = middle(floor, ceiling);
            if (bs.get(i)) {
                floor = mid;
            } else {
                ceiling = mid;
            }
        }
        return mid;
    }

    /**
     * 根据经纬度和范围，获取对应二进制
     */
    private static BitSet getBitSet(BigDecimal lat, BigDecimal floor, BigDecimal ceiling) {
        BitSet buffer = new BitSet(NUM_BITS);
        for (int i = 0; i < NUM_BITS; i++) {
            BigDecimal mid = middle(floor, ceiling);
            if (MathUtils.nvl(lat).compareTo(mid) >= 0) {
                buffer.set(i);
                floor = mid;
            } else {
                ceiling = mid;
            }
        }
        return buffer;
    }

    private static BigDecimal middle(BigDecimal floor, BigDecimal ceiling){
        return (MathUtils.nvl(floor).add(MathUtils.nvl(ceiling))).divide(BD2, 6, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 将经纬度合并后的二进制进行指定的32位编码
     */
    private static String base32(long i) {
        char[] buf = new char[65];
        int charPos = 64;
        boolean negative = (i < 0);
        if (!negative)
            i = -i;
        while (i <= -32) {
            buf[charPos--] = DIGITS[(int) (-(i % 32))];
            i /= 32;
        }
        buf[charPos] = DIGITS[(int) (-i)];

        if (negative)
            buf[--charPos] = '-';
        return new String(buf, charPos, (65 - charPos));
    }

    /**
     * 获取经纬度的 base32字符串
     */
    private static String getGeoHashBase32(BigDecimal lat, BigDecimal lng) {
        boolean[] boolList = getGeoBinary(lat, lng);
        if (boolList == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < boolList.length; i = i + 5) {
            boolean[] base32 = new boolean[5];
            for (int j = 0; j < 5; j++) {
                base32[j] = boolList[i + j];
            }
            char cha = getBase32Char(base32);
            if (' ' == cha) {
                return null;
            }
            sb.append(cha);
        }
        return sb.toString();
    }

    /**
     * 将五位二进制转化为 base32
     */
    private static char getBase32Char(boolean[] base32) {
        if (base32 == null || base32.length != 5) {
            return ' ';
        }
        int num = 0;
        for (boolean bool : base32) {
            num <<= 1;
            if (bool) {
                num += 1;
            }
        }
        return DIGITS[num % DIGITS.length];
    }

    /**
     * 获取坐标的geo二进制字符串
     */
    private static boolean[] getGeoBinary(BigDecimal lat, BigDecimal lng) {
        boolean[] latArray = getHashArray(lat, BD_90, BD90, LAT_LENGTH);
        boolean[] lngArray = getHashArray(lng, BD_180, BD180, LNG_LENGTH);
        return merge(latArray, lngArray);
    }

    /**
     * 合并经纬度二进制
     */
    private static boolean[] merge(boolean[] latArray, boolean[] lngArray) {
        if (latArray == null || lngArray == null) {
            return null;
        }
        boolean[] result = new boolean[lngArray.length + latArray.length];
        Arrays.fill(result, false);
        for (int i = 0; i < lngArray.length; i++) {
            result[2 * i] = lngArray[i];
        }
        for (int i = 0; i < latArray.length; i++) {
            result[2 * i + 1] = latArray[i];
        }
        return result;
    }

    /**
     * 将数字转化为geo hash二进制字符串
     */
    private static boolean[] getHashArray(BigDecimal value, BigDecimal min, BigDecimal max, int length) {
        value = MathUtils.nvl(value);
        if (value.compareTo(min) < 0 || value.compareTo(max) > 0) {
            return null;
        }
        if (length < 1) {
            return null;
        }
        boolean[] result = new boolean[length];
        for (int i = 0; i < length; i++) {
            BigDecimal mid = middle(min, max);
            if (value.compareTo(mid) > 0) {
                result[i] = true;
                min = mid;
            } else {
                result[i] = false;
                max = mid;
            }
        }
        return result;
    }

    /** 计算 hashLength 与 latLength lngLength */
    private static int hashLength = 5, latLength = 0, lngLength = 0;
    public static void main(String[] args) {
        latLength = (hashLength * 5) / 2;
        if (hashLength % 2 == 0) {
            lngLength = latLength;
        } else {
            lngLength = latLength + 1;
        }
        System.out.println("hashLength: " + hashLength + " latLength: " + latLength + " lngLength: " + lngLength);
    }
}