package com.youshi.zebra.user.config.util;

/**
 * 
* 
* Date: May 10, 2016
* 
 * @author wangsch
 *
 * @param <T>
 */
public interface UserConfigCodec<T> {

    String getNamespace();

    T readValue(String node);

    T defaultValue();

    String writeValue(Object value);

    /**
     * 如果是默认值的时候，就从数据源里抹掉
     * 
     * @return
     */
    boolean removeOnDefaultValue();

    default boolean hiddenInConfig() {
        return false;
    }

}
