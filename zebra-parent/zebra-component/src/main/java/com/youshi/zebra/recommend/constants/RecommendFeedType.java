package com.youshi.zebra.recommend.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

/**
 * 
 * 首页Feed推荐类型
 * 
 * @author wangsch
 * @date 2016-09-24
 */
public enum RecommendFeedType {
	TRY_COURSE(0, "试听课"),
	OPEN_COURSE(1, "开课信息"),
	WEIXIN_ARTICLE(2, "资讯"),
	;
	
	private final int value;
	private final String name;
	RecommendFeedType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName() {
    	return name;
    }

    private static final IntObjectMap<RecommendFeedType> map = new IntObjectOpenHashMap<>();
    static {
        for (RecommendFeedType e : RecommendFeedType.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final RecommendFeedType fromValue(Integer status) {
        return map.get(status);
    }
}
