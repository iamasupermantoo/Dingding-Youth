package com.youshi.zebra.core.constants.jedis;

 
import java.util.HashSet;
import java.util.Set;

import com.dorado.framework.jedis.RedisKey;

/**
 * 
 * redis各种key（非缓存）在这注册<br />
 * 
 * key 规范: “前缀” + “.” + “id标识”
 * 
 * @author wangsch
 * @date 2016-09-12
 */
public enum PersistRedisKey implements RedisKey {
	/** 用户secret */
    UserSecret("us."),
    
    /** 消息队列redisKey */
    Event("e."),
    
    RoomRealtimeInfo("ri."),
    RoomAccountGenerator("ra."),
    
    UserStats("ustats."),
    
    SHARE_GET_SCHOLARSHIP("sgs.")
    ;
	
	// TODO 配置
	private static final String PERSIST_PREFIX = "_zp.";
	
    public final String prefix;

    /**
     * @param prefix
     */
    PersistRedisKey(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String of(Object key) {
        if (key != null) {
            return PERSIST_PREFIX + this.prefix + key;
        } else {
            return PERSIST_PREFIX + this.prefix;
        }
    }
    
    static {
        Set<String> keys = new HashSet<>();
        for (PersistRedisKey prefix : PersistRedisKey.values()) {
            boolean add = keys.add(prefix.prefix);
            if (!add) {
                throw new RuntimeException("duplicate redis key prefix found:" + prefix);
            }
        }
    }

//    public static final byte[] encodeInt(int intValue) {
//        return ByteBuffer.allocate(CommonConstants.SIZE_OF_INT).putInt(intValue).array();
//    }
//
//    public static final int decodeInt(byte[] bytes) {
//        return ByteBuffer.wrap(bytes).getInt();
//    }
//
//    public static final byte[] encodeLong(long longValue) {
//        return ByteBuffer.allocate(CommonConstants.SIZE_OF_LONG).putLong(longValue).array();
//    }
//
//    public static final long decodeLong(byte[] bytes) {
//        return ByteBuffer.wrap(bytes).getLong();
//    }

}
