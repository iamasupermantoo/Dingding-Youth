package com.youshi.zebra.order.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

/**
 * 
 * @author wangsch
 * @date 2017年2月18日
 */
public enum PayStatus {
	/**
	 * 未支付
	 */
	NOT_PAID(0, "未支付"), 
	
	/**
	 * 已支付
	 */
	PAID(1, "已支付"),
	;

	private final int value;
	private final String name;

	PayStatus(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	private static final IntObjectMap<PayStatus> map = new IntObjectOpenHashMap<>();
	static {
		for (PayStatus e : PayStatus.values()) {
			map.put(e.getValue(), e);
		}
	}

	public static final PayStatus fromValue(Integer value) {
		return map.get(value);
	}
}
