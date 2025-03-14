package com.youshi.zebra.order.service;

import com.youshi.zebra.order.constants.ProductType;
import com.youshi.zebra.order.model.ProductInfo;
import com.youshi.zebra.pay.constants.PayChannel;

/**
 * 
 * @author wangsch
 * @date 2017年11月8日
 */
public interface OrderHandler {
	/**
	 * 获取商品信息
	 * 
	 * @param productUuid	商品uuid，用户前台传进来的uuid
	 * @param productType	商品类型 {@link ProductType}
	 * @return				商品信息
	 */
	public ProductInfo getProductInfo(String productUuid, ProductType productType);

	/**
	 * 处理支付成功的订单，做逻辑处理。如：增加账户余额
	 * 
	 */
	public void paySucc(String orderSn, PayChannel payChannel);
	
	public void costSucc(Integer userId, String productUuid, Integer type);
	
	/**
	 * @param userId
	 * @param productUuid
	 * @param type
	 */
	public Integer submit(Integer userId, String productUuid, Integer type);

	
	// others 状态处理、重复检测等
	
	
	
}
