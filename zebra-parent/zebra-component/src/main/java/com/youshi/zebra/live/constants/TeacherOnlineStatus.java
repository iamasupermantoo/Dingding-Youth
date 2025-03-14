package com.youshi.zebra.live.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.model.EntityStatus;

public enum TeacherOnlineStatus implements EntityStatus {
	TEACHER_OFFLINE(0, "不在线"),
	TEACHER_ONLINE(1, "老师在线"),
	TEACHER_READY(2, "已准备"),
	TEACHER_ON_PROGRESS(3, "正在直播中"),
	TEACHER_FINISHED(4, "已结束直播"),
//	TEACHER_DISCONNECTED(5, "老师掉线"), 
	;

	private final int value;
	private final String name;

	TeacherOnlineStatus(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public int getValue() {
		return value;
	}

//	public String getName() {
//		return name;
//	}

	private static final IntObjectMap<TeacherOnlineStatus> map = new IntObjectOpenHashMap<>();
	static {
		for (TeacherOnlineStatus e : TeacherOnlineStatus.values()) {
			map.put(e.getValue(), e);
		}
	}

	public static final TeacherOnlineStatus fromValue(Integer value) {
		return map.get(value);
	}
}