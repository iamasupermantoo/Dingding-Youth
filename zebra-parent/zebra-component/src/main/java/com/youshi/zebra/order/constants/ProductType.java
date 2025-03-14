package com.youshi.zebra.order.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.model.EntityStatus;

/**
 * 商品类型
 * 
 * @author wangsch
 * @date 2017年4月17日
 */
public enum ProductType implements EntityStatus {
	COURSE(0, "1v1直播课"), 
	
	CHARGE(1, "充值"),
	
	OPEN_COURSE(2, "1v多直播课"),
	;

	private final int value;
	private final String name;

	ProductType(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	private static final IntObjectMap<ProductType> map = new IntObjectOpenHashMap<>();
	static {
		for (ProductType e : ProductType.values()) {
			map.put(e.getValue(), e);
		}
	}

	public static final ProductType fromValue(Integer value) {
		return map.get(value);
	}
}