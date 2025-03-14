package com.youshi.zebra.reaction.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

/**
 * 
 * @author wangsch
 * @date 2017年2月23日
 */
public enum TeacherReactionIdea {
	Idea_1(1, "优"),
	Idea_2(2, "良"),
	Idea_3(3, "中"),
	Idea_4(4, "差")
	;
	
	private final int value;
	private final String name;
	TeacherReactionIdea(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName() {
    	return name;
    }

    private static final IntObjectMap<TeacherReactionIdea> map = new IntObjectOpenHashMap<>();
    static {
        for (TeacherReactionIdea e : TeacherReactionIdea.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final TeacherReactionIdea fromValue(Integer value) {
        return map.get(value);
    }
}