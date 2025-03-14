package com.youshi.zebra.pay.model;

/**
 * 
 * 微信支付异步通知参数
 * 
 * https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_7&index=3
 * 
 * @author wangsch
 * @date 2017年2月6日
 */
public class WxpayNotify {
//	private String appid;
//	private String mch_id;
//	private String device_info;
//	private String nonce_str;
//	private String sign;
//	private String result_code;
//	private String err_code;
//	private String err_code_des;
//	private String openid;
//	private String is_subscribe;
//	private String trade_type;
//	private String bank_type;
//	private String total_fee;
//	private String fee_type;
	
	private String out_trade_no;
	private String transaction_id;
	
	public String getOut_trade_no() {
		return out_trade_no;
	}
	
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	
	public String getTransaction_id() {
		return transaction_id;
	}
	
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
}
