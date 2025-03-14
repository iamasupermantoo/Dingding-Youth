package com.youshi.zebra.config.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.model.EntityStatus;

/**
 * 
 * @author wangsch
 * @date 2017年2月8日
 */
public enum UserConfigStatus implements EntityStatus {
	Normal(0, "正常"),
	Disabled(1, "禁用"),
	;
	
	private final int value;
	private final String name;
	UserConfigStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName() {
    	return name;
    }

    private static final IntObjectMap<UserConfigStatus> map = new IntObjectOpenHashMap<>();
    static {
        for (UserConfigStatus e : UserConfigStatus.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final UserConfigStatus fromValue(Integer value) {
        return map.get(value);
    }

}
