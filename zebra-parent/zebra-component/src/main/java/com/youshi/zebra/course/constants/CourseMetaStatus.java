package com.youshi.zebra.course.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.model.EntityStatus;

/**
 * 课程meta状态
 * 
 * @author wangsch
 * @date 2017年2月13日
 */
public enum CourseMetaStatus implements EntityStatus {
	Normal(0, "正常，可以选课"),
	/** 
	 * 新添加的或修改后，状态：pending，需要运营人员仔细检查后，手动将pending转换为normal
	 */
	Pending(1, "pending中，不可选课"),
	Unshelved(2, "已下架，不可选课"),
	;
	
	private final int value;
	private final String name;
	CourseMetaStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName() {
    	return name;
    }

    private static final IntObjectMap<CourseMetaStatus> map = new IntObjectOpenHashMap<>();
    static {
        for (CourseMetaStatus e : CourseMetaStatus.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final CourseMetaStatus fromValue(Integer value) {
        return map.get(value);
    }
	
}
