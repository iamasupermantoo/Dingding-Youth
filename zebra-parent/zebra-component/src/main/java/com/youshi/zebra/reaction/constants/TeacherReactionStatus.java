package com.youshi.zebra.reaction.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.model.EntityStatus;

/**
 * 教师给学生的评价，状态
 * 
 * @author wangsch
 * @date 2017年3月28日
 */
public enum TeacherReactionStatus implements EntityStatus {
		WaitCommit(0, "待评价"),
		Committed(1, "已评价"),
		AdminDel(2, "管理员删除")
		;
		
		private final int value;
		private final String name;
		TeacherReactionStatus(int value, String name) {
	        this.value = value;
	        this.name = name;
	    }

	    public int getValue() {
	        return value;
	    }
	    public String getName() {
	    	return name;
	    }

	    private static final IntObjectMap<TeacherReactionStatus> map = new IntObjectOpenHashMap<>();
	    static {
	        for (TeacherReactionStatus e : TeacherReactionStatus.values()) {
	            map.put(e.getValue(), e);
	        }
	    }

	    public static final TeacherReactionStatus fromValue(Integer value) {
	        return map.get(value);
	    }
	}