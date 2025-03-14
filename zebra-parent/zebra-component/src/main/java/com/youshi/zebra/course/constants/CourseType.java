package com.youshi.zebra.course.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年6月27日
 */
public enum CourseType {
	Normal(1, "普通"),
	TRY(2, "试听"),
	;
	
	private final int value;
	private final String name;
	CourseType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName() {
    	return name;
    }

    private static final IntObjectMap<CourseType> map = new IntObjectOpenHashMap<>();
    static {
        for (CourseType e : CourseType.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final CourseType fromValue(Integer value) {
        return map.get(value);
    }
	
}
