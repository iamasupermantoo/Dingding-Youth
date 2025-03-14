package com.youshi.zebra.course.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.model.EntityStatus;

/**
 * 课程状态。BETA
 * 
 * @author wangsch
 * @date 2017年2月13日
 */
public enum CourseStatus implements EntityStatus {
	WaitStart(0, "等待开课"),
	OnProgress(1, "课程正在进行"),
	Finished(2, "课程已经完成"),
	Stoped(3, "课程中途停止（未完成）"),
	Del(4,"课程已下架")
	;
	
	private final int value;
	private final String name;
	CourseStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName() {
    	return name;
    }

    private static final IntObjectMap<CourseStatus> map = new IntObjectOpenHashMap<>();
    static {
        for (CourseStatus e : CourseStatus.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final CourseStatus fromValue(Integer value) {
        return map.get(value);
    }
	
}
