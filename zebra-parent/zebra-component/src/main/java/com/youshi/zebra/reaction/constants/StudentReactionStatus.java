package com.youshi.zebra.reaction.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.model.EntityStatus;

/**
 * 学生给老师的评价，状态
 * 
 * @author wangsch
 * @date 2017年3月28日
 */
public enum StudentReactionStatus implements EntityStatus {
		WaitCommit(0, "待评价"),
		Committed(1, "已评价"),
		AdminDel(2, "管理员删除")
		;
		
		private final int value;
		private final String name;
		StudentReactionStatus(int value, String name) {
	        this.value = value;
	        this.name = name;
	    }

	    public int getValue() {
	        return value;
	    }
	    public String getName() {
	    	return name;
	    }

	    private static final IntObjectMap<StudentReactionStatus> map = new IntObjectOpenHashMap<>();
	    static {
	        for (StudentReactionStatus e : StudentReactionStatus.values()) {
	            map.put(e.getValue(), e);
	        }
	    }

	    public static final StudentReactionStatus fromValue(Integer value) {
	        return map.get(value);
	    }
	}