package com.youshi.zebra.order.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.model.EntityStatus;

/**
 * 
 * 
 * 订单状态。
 * 
 * @author wangsch
 * @date 2017年2月18日
 */
public enum OrderStatus implements EntityStatus {
	/**
	 * 用户提交了预约单
	 */
	USER_COMMITED(0, "预约单已提交"),
	
	/**
	 * 用户提交了订单
	 */
	USER_SUBMITTED(1, "订单已提交"),
	
//	/**
//	 * 订单处理中，销售看到了订单并点击“处理”，转换到该状态。TODO这个状态保留，不知道是否会用到
//	 */
//	ADMIN_PROCESS(1, "订单处理中"),
	
	ADMIN_CONFIRMED(2, "预约单已确认"),
	
	/**
	 * 订单已成功完成
	 */
	FINISHED(3, "订单已成功完成"),
	
	/**
	 * 订单已关闭，未成功完成。如：用户取消订单
	 */
	CLOSED(4, "订单已关闭"),
	;
	
	private final int value;
	private final String name;
	OrderStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName() {
    	return name;
    }

    private static final IntObjectMap<OrderStatus> map = new IntObjectOpenHashMap<>();
    static {
        for (OrderStatus e : OrderStatus.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final OrderStatus fromValue(Integer value) {
        return map.get(value);
    }
	
}
