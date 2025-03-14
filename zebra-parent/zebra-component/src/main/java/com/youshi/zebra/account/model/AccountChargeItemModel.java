package com.youshi.zebra.account.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.youshi.zebra.order.model.ProductPriceModel;

/**
 * 
 * @author codegen
 */
public class AccountChargeItemModel extends AbstractModel<Integer>{
	public enum AccountChargeItemKeys {
		// db 字段
		plus_amount,
		status,
	
		// data字段
	}
	
	private int plusAmount;
	
	public AccountChargeItemModel(
			int id, String data, long createTime, int status,
			int plusAmount
			) {
		super(id, data, createTime, status);
		this.plusAmount = plusAmount;
	}

	public int getPlusAmount() {
		return plusAmount;
	}
	
	// 外部注入price
	private int price;
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	private ProductPriceModel productPrice;
	public void setProductPrice(ProductPriceModel productPrice) {
		this.productPrice = productPrice;
	}
	
	public ProductPriceModel getProductPrice() {
		return productPrice;
	}
}
