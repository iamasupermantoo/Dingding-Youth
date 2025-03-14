package com.youshi.zebra.recommend.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.model.EntityStatus;

/**
 * 焦点图的类型
 * 
 * @author wangsch
 * @date 2017年8月5日
 */
public enum BannerType implements EntityStatus {
	APP(0, "APP 首页"),
    TOPICS_H5(1, "主题 H5"),
    TIMELINE(2, "成长记录")
    ;
    
    private final int value;
	private final String name;
	BannerType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName() {
    	return name;
    }

    private static final IntObjectMap<BannerType> map = new IntObjectOpenHashMap<>();
    static {
        for (BannerType e : BannerType.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final BannerType fromValue(Integer status) {
        return map.get(status);
    }
    
}
