package com.youshi.zebra.homework.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.model.EntityStatus;

/**
 * 
 * 
 * 
 * 
 * @author wangsch
 * @date 2017年2月6日
 */
public enum HomeworkStatus implements EntityStatus {
	WAIT_COMMIT(0, "待提交"),
	WAIT_CORRECT(1, "待评分"),
	CORRECTED(2, "已评分")
	;
	
	private final int value;
	private final String name;
	HomeworkStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName() {
    	return name;
    }

    private static final IntObjectMap<HomeworkStatus> map = new IntObjectOpenHashMap<>();
    static {
        for (HomeworkStatus e : HomeworkStatus.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final HomeworkStatus fromValue(Integer value) {
        return map.get(value);
    }
}
