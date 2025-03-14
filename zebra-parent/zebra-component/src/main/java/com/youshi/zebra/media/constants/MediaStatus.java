package com.youshi.zebra.media.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.model.EntityStatus;

/**
 * 媒体资源状态
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
public enum MediaStatus implements EntityStatus {
		Normal(0, "正常"),
		AdminDel(1, "管理员删除"),
		;
		
		private final int value;
		private final String name;
		MediaStatus(int value, String name) {
	        this.value = value;
	        this.name = name;
	    }

	    public int getValue() {
	        return value;
	    }
	    public String getName() {
	    	return name;
	    }

	    private static final IntObjectMap<MediaStatus> map = new IntObjectOpenHashMap<>();
	    static {
	        for (MediaStatus e : MediaStatus.values()) {
	            map.put(e.getValue(), e);
	        }
	    }

	    public static final MediaStatus fromValue(Integer value) {
	        return map.get(value);
	    }
	}