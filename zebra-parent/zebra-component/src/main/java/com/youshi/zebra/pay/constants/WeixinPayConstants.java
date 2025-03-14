package com.youshi.zebra.pay.constants;

import com.dorado.framework.constants.InProduction;
import com.youshi.zebra.core.constants.config.RawStringConfigKey;

/**
 * 微信支付，常量
 * 
 * @author wangsch
 * @date 2017年2月6日
 */
public class WeixinPayConstants {
	public static final String APP_ID = RawStringConfigKey.WxpayAppid.get();
	
	public static final String PARTNER_ID = RawStringConfigKey.WxpayPartnerid.get();

	public static final String WXPAY_API_SECRET = RawStringConfigKey.WxpayApiSecret.get();

	//统一下单接口
	public static final String WXPAY_UNIFIEDORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	public static final String WXPAY_ORDER_QUERY = "https://api.mch.weixin.qq.com/pay/orderquery";

	public static final String NOTIFY_URL = InProduction.get() ? "http://api.ddlad.com/pay/wx/notify"
			: "http://api.z.ziduan.com/pay/wx/notify";
	
	public static final String LOGGER_NAME = "wxpay";
}
