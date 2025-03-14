package com.youshi.zebra.order.model;

import com.dorado.framework.crud.model.impl.AbstractModel;

/**
 * 
 * @author codegen
 */
public class PreOrderModel extends AbstractModel<Integer>{
	public enum PreOrderKeys {
		// db 字段
		user_id,
		product_id,
		product_type,
		total_price,
		update_time,
	
		// data字段
	}
	
	private int userId;
	private int productId;
	private int productType;
	private int totalPrice;
	private long updateTime;
	
	public PreOrderModel(
			int id, String data, long createTime, int status,
			int userId,
			int productId,
			int productType,
			int totalPrice,
			long updateTime
			) {
		super(id, data, createTime, status);
		this.userId = userId;
		this.productId = productId;
		this.productType = productType;
		this.totalPrice = totalPrice;
		this.updateTime = updateTime;
	}

	public int getUserId() {
		return userId;
	}
	public int getProductId() {
		return productId;
	}
	public int getProductType() {
		return productType;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	
}
