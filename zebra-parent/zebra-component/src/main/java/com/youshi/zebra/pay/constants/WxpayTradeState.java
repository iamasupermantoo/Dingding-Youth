package com.youshi.zebra.pay.constants;

/**
 * 从微信查询到的交易结果枚举，
 * 
 * 接口文档：<a href=
 * "https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_2&index=4">查询订单</a>
 * 
 * 
 * @author wangsch
 * @date 2017年2月6日
 */
public enum WxpayTradeState {
	/** 支付成功 */
	SUCCESS,

	/** 转入退款 */
	REFUND,

	/** 未支付 */
	NOTPAY,

	/** 已关闭 */
	CLOSED,

	/** 已撤销（刷卡支付） */
	REVOKED,

	/** 用户支付中 */
	USERPAYING,

	/** 支付失败(其他原因，如银行返回失败) */
	PAYERROR
}