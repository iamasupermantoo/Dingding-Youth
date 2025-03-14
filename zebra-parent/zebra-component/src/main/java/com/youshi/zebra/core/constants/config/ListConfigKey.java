package com.youshi.zebra.core.constants.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dorado.framework.config.Config.ConfigKey;
import com.github.phantomthief.util.ObjectMapperUtils;

/**
 * List类型的配置key枚举
 * 
 * @author wangsch
 * @date 2016-09-12
 */
public enum ListConfigKey implements ConfigKey<List<Object>> {
	/**
	 * 允许支付测试的用户id，支付金额将设置为1分
	 */
	PAY_TEST_USER_IDS(Integer.class),
    ;

    private final String configKey;

    private final Class<?> clazz;

    /**
     * @param configKey
     */
    ListConfigKey(String configKey) {
        this(configKey, null);
    }

    /**
     * @param configKey
     * @param clazz
     */
    ListConfigKey(String configKey, Class<?> clazz) {
        this.configKey = configKey;
        this.clazz = clazz;
    }

    ListConfigKey(Class<?> clazz) {
        this(null, clazz);
    }

    ListConfigKey() {
        this((String) null);
    }

    @Override
    public String configKey() {
        if (configKey == null) {
            return name();
        } else {
            return configKey;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object> value(String rawValue) throws Exception {
        List<Object> result;
        if (clazz == null) {
            result = ObjectMapperUtils.fromJSON(rawValue, List.class);
        } else {
            result = ObjectMapperUtils.fromJSON(rawValue, List.class, clazz);
        }
        if (result instanceof ArrayList) {
            ((ArrayList<?>) result).trimToSize();
        }
        return result;
    }

    @Override
    public List<Object> defaultValue() {
        return Collections.<Object> emptyList();
    }

}
