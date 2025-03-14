package com.youshi.zebra.pay.model;

/**
 * app唤起支付宝支付，需要的参数信息
 * 
 * @author wangsch
 * @date 2017年4月18日
 */
public class AlipayParams {
	private String orderStr;
	
	public AlipayParams(String orderStr) {
		this.orderStr = orderStr;
	}
	
	public String getOrderStr() {
		return orderStr;
	}
	
	public void setOrderStr(String orderStr) {
		this.orderStr = orderStr;
	}
	
}
