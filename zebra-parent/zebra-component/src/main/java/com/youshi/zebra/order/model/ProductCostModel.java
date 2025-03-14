package com.youshi.zebra.order.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;

/**
 * 
 * @author codegen
 */
public class ProductCostModel extends AbstractModel<Integer>{
	public enum ProductCostKeys {
		// db 字段
		product_id,
		cost,
		original_cost,
		type,
		update_time,
	
		// data字段
	}
	
	private int productId;
	private int cost;
	private int originalCost;
	private int type;
	private long updateTime;
	
	public ProductCostModel(
			int id, String data, long createTime, int status,
			int productId,
			int cost,
			int originalCost,
			int type,
			long updateTime
			) {
		super(id, data, createTime, status);
		this.productId = productId;
		this.cost = cost;
		this.originalCost = originalCost;
		this.type = type;
		this.updateTime = updateTime;
	}

	public int getProductId() {
		return productId;
	}
	public int getCost() {
		return cost;
	}
	public int getOriginalCost() {
		return originalCost;
	}
	public int getType() {
		return type;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	
}
