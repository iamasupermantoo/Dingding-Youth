package com.youshi.zebra.core.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public interface RetrieveCount<K> {

    default int getCount(K key) {
        Integer r = getCounts(Collections.singleton(key)).get(key);
        if (r == null) {
            r = 0;
        }
        return r;
    }

    Map<K, Integer> getCounts(Collection<K> keys);

}
