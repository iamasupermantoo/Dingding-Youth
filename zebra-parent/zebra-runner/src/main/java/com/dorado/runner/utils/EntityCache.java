package com.dorado.runner.utils;

import com.dorado.framework.constants.InProduction;
import com.dorado.framework.crud.service.RetrieveById;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.cache.LoadingCache;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public class EntityCache<K, V> {

    private static final int MAX_SIZE = InProduction.get() ? 1000 : 100;

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    private final LoadingCache<K, V> cache;

    /**
     * @param service
     */
    private EntityCache(RetrieveById<K, V> service) {
        this.cache = CacheBuilder.newBuilder().maximumSize(MAX_SIZE).build(new CacheLoader<K, V>() {

            @Override
            public V load(K key) throws Exception {
                return service.getById(key);
            }
        });
    }

    public static <K, V> EntityCache<K, V> fromService(RetrieveById<K, V> service) {
        return new EntityCache<>(service);
    }

    public V get(K key) {
        try {
            return cache.get(key);
        } catch (InvalidCacheLoadException e) {
            logger.debug("fail to get key:{}", key);
        } catch (Throwable e) {
            logger.error("fail to get key:{}", key, e);
        }
        return null;
    }

}
