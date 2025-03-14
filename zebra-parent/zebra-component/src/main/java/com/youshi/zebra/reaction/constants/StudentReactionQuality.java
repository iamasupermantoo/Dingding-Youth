package com.youshi.zebra.reaction.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

/**
 * 
 * @author wangsch
 * @date 2017年2月23日
 */
public enum StudentReactionQuality {
	Quality_1(1, "课很好，很喜欢"),
	Quality_2(2, "在老师的监督下，学习效果佳"),
	Quality_3(3, "很喜欢听老师的课"),
	Quality_4(4, "老师讲课很好")
	;
	
	private final int value;
	private final String name;
	StudentReactionQuality(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName() {
    	return name;
    }

    private static final IntObjectMap<StudentReactionQuality> map = new IntObjectOpenHashMap<>();
    static {
        for (StudentReactionQuality e : StudentReactionQuality.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final StudentReactionQuality fromValue(Integer value) {
        return map.get(value);
    }
}