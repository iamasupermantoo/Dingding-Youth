package com.youshi.zebra.wx.component.wx.model;

/**
 * 公众号支付，唤起微信需要的参数信息
 * 
 * @author wangsch
 * @date 2017年4月21日
 */
public class WxPayParams {
	private String appId;
	private String timeStamp;
	private String nonceStr;
	private String packageStr;
	private String signType;
	private String paySign;

	public WxPayParams(String appId, String timeStamp, String nonceStr, String packageStr, String signType,
			String paySign) {
		this.appId = appId;
		this.timeStamp = timeStamp;
		this.nonceStr = nonceStr;
		this.packageStr = packageStr;
		this.signType = signType;
		this.paySign = paySign;
	}

	public String getAppId() {
		return appId;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public String getPackage() {
		return packageStr;
	}

	public String getSignType() {
		return signType;
	}

	public String getPaySign() {
		return paySign;
	}
}