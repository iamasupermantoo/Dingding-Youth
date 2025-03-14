package com.youshi.zebra.homework.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

/**
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
public enum AnswerType {
	text(0, "文本"),
	image(1, "图片"),
	audio(2, "语音"),
	;
	
	private final int value;
	private final String name;
	AnswerType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName() {
    	return name;
    }

    private static final IntObjectMap<AnswerType> map = 
    		new IntObjectOpenHashMap<AnswerType>();
    static {
        for (AnswerType e : AnswerType.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final AnswerType fromValue(Integer type) {
        return map.get(type);
    }
}
