package com.youshi.zebra.recommend.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.model.EntityStatus;

/**
 * 首页焦点图的状态
 * 
 * @author wangsch
 * @date		2016年11月7日
 *
 */
public enum BannerStatus implements EntityStatus {
	Online(0, "在线"),
    Offline(1, "下线"),
    Deleted(2, "已删除"),
    ;
    
    private final int value;
	private final String name;
	BannerStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName() {
    	return name;
    }

    private static final IntObjectMap<BannerStatus> map = new IntObjectOpenHashMap<>();
    static {
        for (BannerStatus e : BannerStatus.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final BannerStatus fromValue(Integer status) {
        return map.get(status);
    }
    
}
