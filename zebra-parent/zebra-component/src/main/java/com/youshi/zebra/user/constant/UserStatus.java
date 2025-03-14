package com.youshi.zebra.user.constant;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.model.EntityStatus;
import com.dorado.framework.crud.model.HasStatus;

/**
 * 用户状态
 * 
 * @author wangsch
 * @date 2017年1月10日
 */
public enum UserStatus implements EntityStatus {
	Normal(0, "正常"),
	UserBlocked(1, "用户被封禁"),
	;
	
	private final int value;
	private final String name;
	UserStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName() {
    	return name;
    }

    private static final IntObjectMap<UserStatus> map = new IntObjectOpenHashMap<>();
    static {
        for (UserStatus e : UserStatus.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final UserStatus fromValue(Integer value) {
        return map.get(value);
    }
    
    public static final <T extends HasStatus> boolean isNormal(T item) {
		return item != null && item.getStatus() == UserStatus.Normal.value;
	}

	public static final <T extends HasStatus> boolean isNotNormal(T item) {
		return ! isNormal(item);
	}
}
