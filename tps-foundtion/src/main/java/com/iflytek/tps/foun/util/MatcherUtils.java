package com.iflytek.tps.foun.util;

import org.springframework.util.AntPathMatcher;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by losyn on 4/19/17.
 */
public final class MatcherUtils {
    public static final Pattern EMAIL = Pattern.compile("^([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    private MatcherUtils() {
    }

    public static boolean isEmail(String email){
        return EMAIL.matcher(email).find();
    }

    public static boolean isIpAddress(String ip) {
        String[] p = ip.split("\\.");
        if (p.length != 4) return false;
        for (String pp : p) {
            if (pp.length() > 3) return false;
            int val = Integer.valueOf(pp);
            if (val > 255) return false;
        }
        return true;
    }

    public static void hitAntPathMatcherCache(Set<String> uriSet){
        if (!CollectionUtils.isNullOrEmpty(uriSet)) {
            pathMatcher.setCachePatterns(true);
            for (String pattern : uriSet) {
                // hit on cache
                pathMatcher.match(pattern, "");
            }
        }
    }

    public static boolean isPathMatch(String uri, Set<String> uriSet) {
        for(String pattern: uriSet){
            if(pathMatcher.match(pattern, uri)){
                return true;
            }
        }
        return false;
    }
}
