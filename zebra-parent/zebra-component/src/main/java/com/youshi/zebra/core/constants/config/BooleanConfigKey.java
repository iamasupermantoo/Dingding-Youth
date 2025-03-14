package com.youshi.zebra.core.constants.config;

import com.dorado.framework.config.Config.ConfigKey;

/**
 * 布尔值配置项
 * 
 * @author wangsch
 * @date 2017年1月5日
 */
public enum BooleanConfigKey implements ConfigKey<Boolean> {
	Foo,
	Service(null, true),
    ;

    private final String configKey;

    private final boolean defaultValue;

    /**
     * @param configKey
     * @param defaultValue
     */
    BooleanConfigKey(String configKey, boolean defaultValue) {
        this.configKey = configKey;
        this.defaultValue = defaultValue;
    }

    BooleanConfigKey(String configKey) {
        this(configKey, false);
    }

    BooleanConfigKey() {
        this(null);
    }

    @Override
    public String configKey() {
        if (configKey == null) {
            return name();
        } else {
            return configKey;
        }
    }

    @Override
    public Boolean value(String rawValue) throws Exception {
        return Boolean.parseBoolean(rawValue);
    }

    @Override
    public Boolean defaultValue() {
        return defaultValue;
    }

}
