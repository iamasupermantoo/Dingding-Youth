package com.youshi.zebra.live.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.model.EntityStatus;

/**
 * 直播课，学生在线状态
 * 
 * @author wangsch
 * @date 2017年3月17日
 */
public enum StudentOnlineStatus implements EntityStatus {
	STUDENT_OFFLINE(0, "不在线"),
	STUDENT_ONLINE(1, "学生在线"),
	STUDENT_FINISHED(2, "已结束直播"),
//	STUDENT_DISCONNECTED(3, "学生掉线"), 
	;

	private final int value;
	private final String name;

	StudentOnlineStatus(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	private static final IntObjectMap<StudentOnlineStatus> map = new IntObjectOpenHashMap<>();
	static {
		for (StudentOnlineStatus e : StudentOnlineStatus.values()) {
			map.put(e.getValue(), e);
		}
	}

	public static final StudentOnlineStatus fromValue(Integer value) {
		return map.get(value);
	}
}