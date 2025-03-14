package com.youshi.zebra.pay.model;

import io.swagger.annotations.ApiModel;

/**
 * app唤起苹果支付，需要的参数
 * 
 * @author wangsch
 * @date 2017年4月18日
 */
@ApiModel("苹果支付参数")
public class ApplePayParams {
	private String appleProductId;
	
	public ApplePayParams(String appleProductId) {
		this.appleProductId = appleProductId;
	}

	public String getAppleProductId() {
//		return "year199";
		return appleProductId;
	}
}