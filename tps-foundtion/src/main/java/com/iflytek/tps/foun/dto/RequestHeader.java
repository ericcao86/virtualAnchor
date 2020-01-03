package com.iflytek.tps.foun.dto;


import com.iflytek.tps.foun.util.EnumUtils;

/**
 * Created by losyn on 6/11/17.
 */
public enum RequestHeader {
    /** 请求数据格式，如： application/json */
    ContentType("Content-Type"),

    /** 请求返回类型，如： HTML, JSON；默认返回 HTML */
    ReplyDataType("X-Reply-Type"),

    /** APP 版本， 如： V_1.0, V_2.0, V_3.0 */
    Vnum("X-Vnum"),

    /** APP 下载渠道, 如： IOSAppStore, AndroidAppMarket, WeChatSubscription */
    Channel("X-Channel"),

    /** 设备类型， 如: IOS, Android, Tablet, PC */
    Device("X-Device"),

    /** 设备编号，每个设备的唯一标识，如 PC 的 MAC 地址 */
    Udid("X-Udid"),

    /** 请求时的客户端时间戳毫秒数 */
    ClientTime("X-Client-Time"),

    /** 验证公钥 */
    PubKey("X-Pub-Key"),

    /** 验证 */
    Signature("X-Signature"),

    /** 针对以上 Header 参数的 MD5 验签 */
    Authorization("X-Authorization");

    RequestHeader(String value) {
        EnumUtils.changeNameTo(this, value);
    }
}
