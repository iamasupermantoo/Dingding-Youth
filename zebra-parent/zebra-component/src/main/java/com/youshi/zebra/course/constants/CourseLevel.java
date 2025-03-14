package com.youshi.zebra.course.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

/**
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
public enum CourseLevel {
	_1(1, "初级"),
	_2(2, "中级"),
	_3(3, "高级"),
	_4(4, "高级"),
	_5(5, "高级"),
	_6(6, "高级"),
	
	_7(7, "高级"),
	_8(8, "高级"),
	_9(9, "高级"),
	_10(10, "高级"),
	_11(11, "高级"),
	_12(12, "高级"),
	;
	
	private final int value;
	private final String name;
	CourseLevel(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName() {
    	return name;
    }

    private static final IntObjectMap<CourseLevel> map = new IntObjectOpenHashMap<>();
    static {
        for (CourseLevel e : CourseLevel.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final CourseLevel fromValue(Integer value) {
        return map.get(value);
    }
	
}
