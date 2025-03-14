package com.youshi.zebra.core.constants.config;

import com.dorado.framework.config.Config.ConfigKey;

/**
 * 
 * 字符串配置项
 * 
 * @author wangsch
 * @date 2017年1月5日
 */
public enum RawStringConfigKey implements ConfigKey<String> {
	Foo,
	Service,
	ServiceCode(null, "42gEfOPG_lzW16qv20-Hlg"),
	AlipayAppid,
	AliWebPayAppPublicKey,
	AlipayAppPrivateKey, 
	AlipayAppPublicKey,
    AlipayPublicKey,
    AlipaySellerId,
	
	WxpayAppid,
	WxpayPartnerid,
    WxpayApiSecret,
    
    AliyunRAMAccessKeyId,
    AliyunRAMAccessKeySecret,
    
    STATS_TODAY, 
    
    AliWebPayAppid, 
    AliWebPayAppPrivateKey, 
    AliWebPaySellerId, 
    AliWebPayPublicKey, 
    WxFwhAppId, 
    WxFwhPayPartnerid, 
    WxFwhPayApiSecret, 
    
    WxFwhSecret, 
    WxFwhVerifyToken,
    
    CoursePhone// TODO
    , 
    ServicePhone, 
    
    WxAppid, WxSecret
    ;

    private final String configKey;

    private final String defaultValue;

    /**
     * @param configKey
     * @param defaultValue
     */
    RawStringConfigKey(String configKey, String defaultValue) {
        this.configKey = configKey;
        this.defaultValue = defaultValue;
    }

    RawStringConfigKey(String configKey) {
        this(configKey, null);
    }

    RawStringConfigKey() {
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
    public String value(String rawValue) throws Exception {
        return rawValue;
    }

    @Override
    public String defaultValue() {
        return defaultValue;
    }

}
