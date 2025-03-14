package com.youshi.zebra.reaction.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

/**
 * 
 * @author wangsch
 * @date 2017年2月23日
 */
public enum TeacherReactionBehavior {
	Behavior_1(1, "优"),
	Behavior_2(2, "良"),
	Behavior_3(3, "中"),
	Behavior_4(4, "差")
	;
	
	private final int value;
	private final String name;
	TeacherReactionBehavior(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName() {
    	return name;
    }

    private static final IntObjectMap<TeacherReactionBehavior> map = new IntObjectOpenHashMap<>();
    static {
        for (TeacherReactionBehavior e : TeacherReactionBehavior.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final TeacherReactionBehavior fromValue(Integer value) {
        return map.get(value);
    }
}