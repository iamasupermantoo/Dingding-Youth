package com.youshi.zebra.pay.constants;

/**
 * 支付宝通知时，收到的“支付状态”枚举如下。
 * 
 * @author wangsch
 * @date 2017年2月6日
 */
public enum AlipayTradeStatus {
	/**
	 * 交易创建，等待买家付款
	 */
	WAIT_BUYER_PAY,

	/**
	 * 未付款交易超时关闭，或支付完成后全额退款
	 */
	TRADE_CLOSED,

	/**
	 * 交易支付成功
	 */
	TRADE_SUCCESS,

	/**
	 * 交易结束，不可退款
	 */
	TRADE_FINISHED
}