package com.youshi.zebra.live.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

public enum AgoraLiveRole {
	Broadcaster(1, "主播"), 
	Audience(2, "观看者"),
	;

	private final int value;
	private final String name;

	AgoraLiveRole(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	private static final IntObjectMap<AgoraLiveRole> map = new IntObjectOpenHashMap<>();
	static {
		for (AgoraLiveRole e : AgoraLiveRole.values()) {
			map.put(e.getValue(), e);
		}
	}

	public static final AgoraLiveRole fromValue(Integer value) {
		return map.get(value);
	}
}