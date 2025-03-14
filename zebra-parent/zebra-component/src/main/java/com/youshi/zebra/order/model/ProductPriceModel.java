package com.youshi.zebra.order.model;

import com.dorado.framework.crud.model.impl.AbstractModel;

/**
 * 
 * @author codegen
 */
public class ProductPriceModel extends AbstractModel<Integer>{
	public enum ProductPriceKeys {
		// db 字段
		product_id,
		apple_product_id,
		price,
		original_price,
		type,
		update_time,
	
		// data字段
		
	}
	
	private int productId;
	private String appleProductId;
	private int price;
	private int originalPrice;
	private int type;
	private long updateTime;
	
	public ProductPriceModel(
			int id, String data, long createTime, int status,
			int productId,
			String appleProductId,
			int price,
			int originalPrice,
			int type,
			long updateTime
			) {
		super(id, data, createTime, status);
		this.productId = productId;
		this.appleProductId = appleProductId;
		this.price = price;
		this.originalPrice = originalPrice;
		this.type = type;
		this.updateTime = updateTime;
	}

	public int getProductId() {
		return productId;
	}
	public String getAppleProductId() {
		return appleProductId;
	}
	public int getPrice() {
		return price;
	}
	public int getOriginalPrice() {
		return originalPrice;
	}
	public int getType() {
		return type;
	}
	public long getUpdateTime() {
		return updateTime;
	}
}
