package com.youshi.zebra.homework.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.model.EntityStatus;

/**
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
public enum HomeworkAnswerStatus implements EntityStatus {
	Normal(0, "正常"),
	UserDel(1, "用户删除"),
	AdminDel(2, "管理员删除")
	;
	
	private final int value;
	private final String name;
	HomeworkAnswerStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName() {
    	return name;
    }

    private static final IntObjectMap<HomeworkAnswerStatus> map = new IntObjectOpenHashMap<>();
    static {
        for (HomeworkAnswerStatus e : HomeworkAnswerStatus.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final HomeworkAnswerStatus fromValue(Integer value) {
        return map.get(value);
    }
}
