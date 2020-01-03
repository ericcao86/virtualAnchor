package com.iflytek.tps.foun.util;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.util.SafeEncoder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Set;
import java.util.SortedMap;

/**
 * 带虚拟节点的分布式一致性 Hash运算
 */
public final class DestHashUtils {
    private static final Logger LOG = LoggerFactory.getLogger(DestHashUtils.class);

    private static final int VIRTUAL_NODE_NUM = 10;

    private DestHashUtils() {
    }

    /**
     * 一致性Hash算法筛选节点
     */
    public static String targetNode(String src, Set<String> destNodes) {
        if(StringUtils.isBlank(src) || CollectionUtils.isNullOrEmpty(destNodes)){
            return StringUtils.defaultIfBlank(src, StringUtils.EMPTY);
        }
        //生成 Hash 环
        SortedMap<Long, String> hashCircle = makeHashRing(destNodes);
        // 生成 src Hash 值
        long hash = hash(src);
        if(LOG.isDebugEnabled()) {
            LOG.debug("src: {}, hash: {}", src, hash);
        }
        //返回此映射的部分视图，其键大于等于 hash
        return targetNode(hash, hashCircle);
    }

    /** 顺时针取 Hash 环的值 **/
    public static String targetNode(long hash, SortedMap<Long, String> hashRingMap){
        if(CollectionUtils.isNullOrEmpty(hashRingMap)){
            throw new RuntimeException("hash ring map is null or empty.....");
        }
        SortedMap<Long, String> tailMap = hashRingMap.tailMap(hash);
        hash = tailMap.isEmpty() ? hashRingMap.firstKey() : tailMap.firstKey();
        return hashRingMap.get(hash);
    }

    /** 生成 HASH 环 **/
    public static SortedMap<Long, String> makeHashRing(Set<String> nodes){
        SortedMap<Long, String> hashCircle = Maps.newTreeMap();
        for(String node: nodes){
            for(int i = 0; i < VIRTUAL_NODE_NUM; i++){
                hashCircle.put(hash(node + i), node);
            }
        }
        return hashCircle;
    }

    /** 生成 Hash 值 **/
    public static long hash(String key) {
        ByteBuffer buf = ByteBuffer.wrap(SafeEncoder.encode(key));
        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        long m = 0xc6a4a7935bd1e995L;
        int r = 47;
        long h = 0x1234ABCD ^ (buf.remaining() * m);
        long k;
        while (buf.remaining() >= 8) {
            k = buf.getLong();
            k *= m;
            k ^= k >>> r;
            k *= m;
            h ^= k;
            h *= m;
        }
        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
            finish.put(buf).rewind();
            h ^= finish.getLong();
            h *= m;
        }
        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;
        buf.order(byteOrder);
        return h;
    }
}