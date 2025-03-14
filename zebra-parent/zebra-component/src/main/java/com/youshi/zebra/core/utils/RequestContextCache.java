package com.youshi.zebra.core.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.dorado.framework.utils.RetrieveIdUtils.IMultiDataAccess;
import com.google.common.collect.Maps;

/**
 * 在当前的HTTP请求上下文中，存储Model的缓存。目的是，在同一个请求的处理过程中，
 * 将非常快的能够取出来Model使用而不用访问redis或者db，缺点是消耗一些本地jvm的内存。
 * 
 * Note:
 * 基于{@link org.springframework.web.context.request.RequestContextHolder}实现，要求在WEB环境中，
 * 因为被缓存对象是存在本线程（很可能是ThreadLocal中）。
 * 
 * 在非Web环境下，这个类起不到任何作用，不会保存任何数据。
 * 
 * @author wangsch
 * @author wangsch
 *
 * @date 2016-09-12
 */
public class RequestContextCache<K, V> extends RequestContextHolder implements
        IMultiDataAccess<K, V> {

    private static final String PREFIX = "__ctl_";

    private static final int THREAD_LOCAL_NAME_LENGTH = 3;

    private static final Set<String> ALL_NAMES = Collections.synchronizedSet(new HashSet<String>());

    private String uniqueNameForRequestContext;

    public RequestContextCache() {
        String uniqName;
        do {
            uniqName = PREFIX + RandomStringUtils.randomAlphanumeric(THREAD_LOCAL_NAME_LENGTH);
        } while (!ALL_NAMES.add(uniqName));
        uniqueNameForRequestContext = uniqName;
    }

    @SuppressWarnings("unchecked")
    private final Map<K, V> init() {
        try {
            RequestAttributes attrs = currentRequestAttributes();
            ConcurrentHashMap<K, V> concurrentHashMap = (ConcurrentHashMap<K, V>) attrs
                    .getAttribute(uniqueNameForRequestContext, RequestAttributes.SCOPE_REQUEST);
            if (concurrentHashMap == null) {
                synchronized (attrs) {
                    concurrentHashMap = (ConcurrentHashMap<K, V>) attrs.getAttribute(
                            uniqueNameForRequestContext, RequestAttributes.SCOPE_REQUEST);
                    if (concurrentHashMap == null) {
                        concurrentHashMap = new ConcurrentHashMap<>();
                        attrs.setAttribute(uniqueNameForRequestContext, concurrentHashMap,
                                RequestAttributes.SCOPE_REQUEST);
                    }
                }
            }
            return concurrentHashMap;
        } catch (IllegalStateException e) {
            return null;
        }
    }

    public V get(K key) {
        return get(Collections.singleton(key)).get(key);
    }

    @Override
    public Map<K, V> get(Collection<K> keys) {
        Map<K, V> thisCache;
        if (CollectionUtils.isNotEmpty(keys) && (thisCache = init()) != null) {
            return Collections.unmodifiableMap(Maps.filterKeys(thisCache, keys::contains));
        } else {
            return Collections.emptyMap();
        }
    }

    @Override
    public void set(Map<K, V> dataMap) {
        Map<K, V> thisMap;
        if (dataMap != null && (thisMap = init()) != null) {
            thisMap.putAll(dataMap);
        }
    }

    public void set(K key, V value) {
        set(Collections.singletonMap(key, value));
    }

    public void remove(K key) {
        Map<K, V> thisMap = init();
        if (thisMap != null) {
            thisMap.remove(key);
        }
    }

}
