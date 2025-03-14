package com.youshi.zebra.book.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.model.EntityStatus;

/**
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
public enum ChapterStatus implements EntityStatus{
		Normal(0, "正常"),
		AdminDel(1, "管理员删除"),
		;
		
		private final int value;
		private final String name;
		ChapterStatus(int value, String name) {
	        this.value = value;
	        this.name = name;
	    }

	    public int getValue() {
	        return value;
	    }
	    public String getName() {
	    	return name;
	    }

	    private static final IntObjectMap<ChapterStatus> map = new IntObjectOpenHashMap<>();
	    static {
	        for (ChapterStatus e : ChapterStatus.values()) {
	            map.put(e.getValue(), e);
	        }
	    }

	    public static final ChapterStatus fromValue(Integer value) {
	        return map.get(value);
	    }
	}