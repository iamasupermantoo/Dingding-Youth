package com.youshi.zebra.user.constant;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

/**
 * 用户类型
 * 
 * @author wangsch
 * @date 2017年1月11日
 */
public enum UserType {
	Student(0, "学生"),
	Teacher(1, "老师"),
	Admin(3, "运营管理人员")
	;
	
	private final int value;
	private final String name;
	UserType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName() {
    	return name;
    }

    private static final IntObjectMap<UserType> map = new IntObjectOpenHashMap<>();
    static {
        for (UserType e : UserType.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final UserType fromValue(Integer value) {
        return map.get(value);
    }
}
