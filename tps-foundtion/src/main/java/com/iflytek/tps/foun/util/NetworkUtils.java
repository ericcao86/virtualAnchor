package com.iflytek.tps.foun.util;


import com.iflytek.tps.foun.dto.RequestHeader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public final class NetworkUtils {
    private final static Logger LOG = LoggerFactory.getLogger(NetworkUtils.class);
    public final static String REPLY_DATA_TYPE = "JSON";

    private NetworkUtils() {
    }

    public static String ofInnerIp(){
        return ofServerAddress(IpMode.Inner).getHostAddress();
    }

    public static String ofOuterIp(){
        return ofServerAddress(IpMode.Outer).getHostAddress();
    }

    public static InetAddress ofServerAddress(IpMode mode) {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if(isFictitious(networkInterface.getName())){
                    continue;
                }
                Enumeration<InetAddress> inAddresses = networkInterface.getInetAddresses();
                while (inAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inAddresses.nextElement();
                    String ip = inetAddress.getHostAddress();
                    if(!StringUtils.isBlank(ip) && inetAddress instanceof Inet4Address){
                        switch (mode){
                            case Inner:
                                if(inetAddress.isSiteLocalAddress() && !inetAddress.isAnyLocalAddress()){
                                    return inetAddress;
                                }
                                break;
                            case Outer:
                                if(!inetAddress.isLoopbackAddress() && !inetAddress.isSiteLocalAddress()){
                                    return inetAddress;
                                }
                                break;
                            case LoopBack:
                                if(inetAddress.isLoopbackAddress()){
                                    return inetAddress;
                                }
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("of server address error: {}", e);
        }
        throw new RuntimeException("can not find server real address....");
    }

    public static long ip2long(String ip) {
        String[] p = ip.split("\\.");
        if (p.length != 4) {
            return 0;
        }
        int p1 = ((Integer.valueOf(p[0]) << 24) & 0xFF000000);
        int p2 = ((Integer.valueOf(p[1]) << 16) & 0x00FF0000);
        int p3 = ((Integer.valueOf(p[2]) << 8) & 0x0000FF00);
        int p4 = ((Integer.valueOf(p[3]) << 0) & 0x000000FF);
        return ((p1 | p2 | p3 | p4) & 0xFFFFFFFFL);
    }

    public static String long2ip(long ip) {
        StringBuilder sb = new StringBuilder();
        sb.append((ip >> 24) & 0xFF).append('.')
                .append((ip >> 16) & 0xFF).append('.')
                .append((ip >> 8) & 0xFF).append('.')
                .append((ip >> 0) & 0xFF);
        return sb.toString();
    }

    public static String ofClientIp(HttpServletRequest request) {
        String fromSource = "X-Real-IP";
        String ip = request.getHeader("X-Real-IP");
        if (noMappingIp(ip)) {
            ip = request.getHeader("X-Forwarded-For");
            fromSource = "X-Forwarded-For";
        }
        if (noMappingIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            fromSource = "Proxy-Client-IP";
        }
        if (noMappingIp(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            fromSource = "WL-Proxy-Client-IP";
        }
        if (noMappingIp(ip)) {
            ip = request.getRemoteAddr();
            fromSource = "request.getRemoteAddr";
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("App Client IP: " + ip + ", fromSource: " + fromSource);
        }
        return ip;
    }

    public static boolean needReplyJson(HttpServletRequest request) {
        return REPLY_DATA_TYPE.equalsIgnoreCase(request.getHeader(RequestHeader.ReplyDataType.name()));
    }

    private static boolean noMappingIp(String ip) {return StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip);}

    private static boolean isFictitious(String name) {
        return "docker0".equals(name);
    }

    public enum IpMode {
        Inner,
        Outer,
        LoopBack
    }
}
