package com.youshi.zebra.order.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.model.EntityStatus;

/**
 * 订单的电聊结果
 * 
 * @author wangsch
 * @date 2017年6月20日
 */
public enum ChatResult implements EntityStatus {
	NO(0, "未电聊"),
	
	_1(1, "客户没兴趣"),
	
	_2(2, "可下单"),
	
	_3(3, "其他"),
	;
	
	private final int value;
	private final String name;
	ChatResult(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName() {
    	return name;
    }

    private static final IntObjectMap<ChatResult> map = new IntObjectOpenHashMap<>();
    static {
        for (ChatResult e : ChatResult.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final ChatResult fromValue(Integer value) {
        return map.get(value);
    }
	
}
