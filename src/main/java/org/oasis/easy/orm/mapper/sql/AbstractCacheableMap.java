package org.oasis.easy.orm.mapper.sql;

import com.google.common.cache.*;

import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author tianbo
 * @date 2019-01-10
 */
public abstract class AbstractCacheableMap<K, V> {

    // 默认缓存的记录数
    private static final long MAX_SIZE = 100;

    // 默认的缓存失效时间
    private static final long DURATION = 5;

    // 默认的缓存失效时间的单位
    private static final TimeUnit TIME_UNIT = TimeUnit.MINUTES;

    private final LoadingCache<K, V> cache;

    public AbstractCacheableMap() {
        this(MAX_SIZE, DURATION, TIME_UNIT);
    }

    public AbstractCacheableMap(long capacity, long expire, TimeUnit timeUnit) {
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(capacity)
                .expireAfterAccess(expire, timeUnit)
                .build(new CacheLoader<K, V>() {
                    @Override
                    public V load(K key) throws Exception {
                        return newValueObject(key);
                    }
                });
    }

    public V get(K key) {
        return cache.getIfPresent(key);
    }

    public V getOrCreate(K key) {
        try {
            return cache.get(key);
        } catch (ExecutionException eex) {
            throw new EasyOrmException(ErrorCode.SERVICE_ERROR, "get or create object failed");
        }
    }

    /**
     * 对象如何创建,交由实现类自行确定
     */
    protected abstract V newValueObject(K key);

}
