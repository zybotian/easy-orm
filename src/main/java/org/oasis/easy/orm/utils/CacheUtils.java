package org.oasis.easy.orm.utils;

import org.apache.commons.collections4.map.LRUMap;

import java.util.Collections;
import java.util.Map;

/**
 * @author tianbo
 * @date 2018-12-25
 */
public class CacheUtils {

    /**
     * 最大缓存size为10万对K-V
     */
    private static final int MAX_SIZE = 100_000;
    /**
     * 默认缓存大小为100对K-V
     */
    private static final int DEFAULT_SIZE = 100;

    public static <K, V> Map<K, V> getSynchronizedLRUCache() {
        return getSynchronizedLRUCache(DEFAULT_SIZE);
    }

    public static <K, V> Map<K, V> getSynchronizedLRUCache(int maxSize) {
        maxSize = maxSize > 0 ? maxSize : DEFAULT_SIZE;
        maxSize = Math.min(maxSize, MAX_SIZE);
        return Collections.synchronizedMap(new LRUMap<K, V>(maxSize));
    }
}
