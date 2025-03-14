package com.youshi.zebra.core.constants.config;

import com.dorado.framework.config.Config.ConfigKey;

/**
 * Int配置项
 * 
 * @author wangsch
 * @date 2017年1月5日
 */
public enum IntConfigKey implements ConfigKey<Integer> {
	/**
	 * 默认的头像id
	 */
    DefaultHeadImageId("DefaultHeadImageId"), 
    RetainMinAmount("RetainMinAmount", 10000), 
    SharePlusAmount("SharePlusAmount", 5),
    ;

    private final String configKey;

    private final int defaultValue;

    /**
     * @param configKey
     * @param defaultValue
     */
    IntConfigKey(String configKey, int defaultValue) {
        this.configKey = configKey;
        this.defaultValue = defaultValue;
    }

    IntConfigKey(String configKey) {
        this(configKey, 0);
    }

    IntConfigKey() {
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
    public Integer value(String rawValue) throws Exception {
        return Integer.parseInt(rawValue);
    }

    @Override
    public Integer defaultValue() {
        return defaultValue;
    }

}
