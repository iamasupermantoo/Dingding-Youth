package com.youshi.zebra.media.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

/**
 * 媒体资源类型
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
public enum MediaType {
		Image(0, "图片"),
		Audio(1, "音频"),
		Video(2, "视频"),
		
		Text(3, "文本"),
		
		;
		
		private final int value;
		private final String name;
		MediaType(int value, String name) {
	        this.value = value;
	        this.name = name;
	    }

	    public int getValue() {
	        return value;
	    }
	    public String getName() {
	    	return name;
	    }

	    private static final IntObjectMap<MediaType> map = new IntObjectOpenHashMap<>();
	    static {
	        for (MediaType e : MediaType.values()) {
	            map.put(e.getValue(), e);
	        }
	    }

	    public static final MediaType fromValue(Integer value) {
	        return map.get(value);
	    }
	}