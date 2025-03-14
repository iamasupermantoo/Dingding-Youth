package com.youshi.zebra.core.constants.localcache;

import com.dorado.framework.localcache.NotifyAble;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public enum LocalCacheNotifyKey implements NotifyAble {
	SpammerDict("spammer_dict_nofiy_key"),
    SpammerKeywordSet("spammer_keyword_set"),
    
    /** api接口常量 */
    APIConstant("api_constant"),
    
    /** 客户端版本记录，cache */
    ClientVersion("client_version"),
    ;

    /**
     * @param key
     */
    LocalCacheNotifyKey(String key) {
        this.key = key;
    }
	
	// TODO 配置项目
    private static final String PREFIX = "zebra_";

    private final String key;

    @Override
    public String getKey() {
        return PREFIX + key;
    }
}
