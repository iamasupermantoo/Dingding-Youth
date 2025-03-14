package com.youshi.zebra.pay.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.youshi.zebra.order.constants.ProductType;

/**
 * 
 * 这个类代表，苹果支付的一个商品。苹果支付的商品，需要关联到系统内的商品，
 * 通过{@link #productid}和{@link #appleProductId}建立这种一一映射
 * 
 * @author wangsch
 * @date 2017年4月17日
 */
public class AppleProductModel extends AbstractModel<Integer>{
	/** 系统内商品id */
	private int productId;
	
	/** {@link ProductType} */
	private int type;
	
	/** 苹果支付的商品id */
	private String appleProductId;
	
	public AppleProductModel(Integer id, String data, long createTime, int status,
			int productId, String appleProductId, int type) {
		super(id, data, createTime, status);
		this.productId = productId;
		this.appleProductId = appleProductId;
		this.type = type;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getAppleProductId() {
		return appleProductId;
	}

	public void setAppleProductId(String appleProductId) {
		this.appleProductId = appleProductId;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
}
