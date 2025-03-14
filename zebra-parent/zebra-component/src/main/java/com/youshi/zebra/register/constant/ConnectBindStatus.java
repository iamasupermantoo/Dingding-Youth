package com.youshi.zebra.register.constant;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

/**
 * 
 * connect绑定状态
 * 
 * Date: May 10, 2016
 * 
 * @author wangsch
 *
 */
public enum ConnectBindStatus {
    /**
     * 从未绑定
     */
    NeverBinded(0, "从未绑定"),
    
    /**
     * 已绑定
     */
    Binded(1, "已绑定"),
    
    /**
     * 已解绑（之前绑定过）
     */
    Unbinded(2, "已解绑（之前绑定过）"),
	;
	
	private final int value;
	private final String name;
	ConnectBindStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName() {
    	return name;
    }

    private static final IntObjectMap<ConnectBindStatus> map = new IntObjectOpenHashMap<>();
    static {
        for (ConnectBindStatus e : ConnectBindStatus.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final ConnectBindStatus fromValue(Integer value) {
        return map.get(value);
    }
    
    
    
}
