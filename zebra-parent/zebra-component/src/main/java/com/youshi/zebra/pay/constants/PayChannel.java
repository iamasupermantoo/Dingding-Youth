package com.youshi.zebra.pay.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

/**
 * 
 * 支付渠道
 * 
 */
public enum PayChannel {
	WEIXIN(0,"微信支付"),
    ALIPAY(1,"支付宝"),
    APPLE(2,"苹果支付"),
    WEIXIN_FWH(3, "微信服务号"),
    
    ADMIN(99, "后台免支付")
    
	;
	
	private final int value;
	private final String name;
	
	PayChannel(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName() {
    	return name;
    }

    private static final IntObjectMap<PayChannel> map = new IntObjectOpenHashMap<>();
    static {
        for (PayChannel e : PayChannel.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final PayChannel fromValue(Integer value) {
        return map.get(value);
    }

	
}
