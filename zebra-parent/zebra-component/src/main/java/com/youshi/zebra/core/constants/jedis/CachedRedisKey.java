package com.youshi.zebra.core.constants.jedis;

import java.util.HashSet;
import java.util.Set;

import com.dorado.framework.jedis.RedisKey;

/**
 * redis实体缓存，key都在这注册
 * 
 * @author wangsch
 * @date 2016-09-12
 */
public enum CachedRedisKey implements RedisKey {
    Image("i:"),
    User("u:"),
    ;
	
	private static final String CACHE_PREFIX = "_zc.";
	
    private final String prefix;

    /**
     * @param prefix
     */
    CachedRedisKey(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String of(Object key) {
        if (key != null) {
            return CACHE_PREFIX + this.prefix + key;
        } else {
            return CACHE_PREFIX + this.prefix;
        }
    }

    static {
        Set<String> keys = new HashSet<>();
        for (CachedRedisKey prefix : CachedRedisKey.values()) {
            boolean add = keys.add(prefix.prefix);
            if (!add) {
                throw new RuntimeException("duplicate redis key prefix found:" + prefix);
            }
        }
    }

}
