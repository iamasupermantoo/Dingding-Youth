package com.youshi.zebra.pay.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 客户端唤起支付客户端，需要的参数信息
 * 
 * @author wangsch
 * @date 2017年4月18日
 */
@ApiModel("客户端唤起支付客户端，需要的参数信息")
public class PayParams {
	private ApplePayParams apple;
	private WeixinPayParams weixin;
	private AlipayParams alipay;
	
	private String orderSn;

	@ApiModelProperty(value = "苹果支付参数")
	public ApplePayParams getApple() {
		return apple;
	}

	public void setApple(ApplePayParams apple) {
		this.apple = apple;
	}
	
	@ApiModelProperty(value = "微信支付参数")
	public WeixinPayParams getWeiXin() {
		return weixin;
	}

	public void setWeixin(WeixinPayParams weixin) {
		this.weixin = weixin;
	}
	
	@ApiModelProperty(value = "支付宝支付参数")
	public AlipayParams getAlipay() {
		return alipay;
	}
	
	public void setAlipay(AlipayParams alipay) {
		this.alipay = alipay;
	}
	
	@ApiModelProperty(value = "订单编号")
	public String getOrderSn() {
		return orderSn;
	}
	
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
}