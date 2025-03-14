package com.youshi.zebra.core.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.dorado.framework.tuple.Tuple;
import com.dorado.framework.tuple.TwoTuple;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public class MultiKeyRequestContextCache<K1, K2, V> extends
        RequestContextCache<TwoTuple<K1, K2>, V> {

    public Map<K1, V> getByKey1(K2 key2, Collection<K1> keys) {
        Map<TwoTuple<K1, K2>, V> map = get(keys.stream().map(k1 -> Tuple.tuple(k1, key2))
                .collect(Collectors.toSet()));
        return map.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().getFirst(), Entry::getValue));
    }

    public Map<K2, V> getByKey2(K1 key1, Collection<K2> keys) {
        Map<TwoTuple<K1, K2>, V> map = get(keys.stream().map(k2 -> Tuple.tuple(key1, k2))
                .collect(Collectors.toSet()));
        return map.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().getSecond(), Entry::getValue));
    }

    public void setKey1(K1 key1, Map<K2, V> data) {
        set(data.entrySet()
                .stream()
                .collect(
                        Collectors.toMap(e -> Tuple.<K1, K2> tuple(key1, e.getKey()),
                                Entry::getValue)));
    }

    public void setKey2(K2 key2, Map<K1, V> data) {
        set(data.entrySet()
                .stream()
                .collect(
                        Collectors.toMap(e -> Tuple.<K1, K2> tuple(e.getKey(), key2),
                                Entry::getValue)));
    }

    public void remove(K1 key1, K2 key2) {
        remove(Tuple.tuple(key1, key2));
    }
}
