package com.youshi.zebra.live.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.model.EntityStatus;

/**
 * 直播课，家长在线状态
 * 
 * @author wangsch
 * @date 2017年3月17日
 */
public enum ParentOnlineStatus implements EntityStatus {
	PARENT_JOINED(2, "家长已加入房间"),
	PARENT_DISCONNECTED(3, "家长掉线"), 
	;

	private final int value;
	private final String name;

	ParentOnlineStatus(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	private static final IntObjectMap<ParentOnlineStatus> map = new IntObjectOpenHashMap<>();
	static {
		for (ParentOnlineStatus e : ParentOnlineStatus.values()) {
			map.put(e.getValue(), e);
		}
	}

	public static final ParentOnlineStatus fromValue(Integer value) {
		return map.get(value);
	}
}