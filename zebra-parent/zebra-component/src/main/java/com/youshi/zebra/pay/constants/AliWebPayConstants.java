package com.youshi.zebra.pay.constants;

import com.dorado.framework.constants.InProduction;
import com.youshi.zebra.core.constants.config.RawStringConfigKey;

/**
 * 
 * 支付宝常量
 * 
 * @author wangsch
 * @date 2017年2月4日
 */
public class AliWebPayConstants {
	public static final String APP_ID = RawStringConfigKey.AliWebPayAppid.get();
	public static final String APP_PUBLIC_KEY = RawStringConfigKey.AliWebPayAppPublicKey.get();
	public static final String APP_PRIVATE_KEY = RawStringConfigKey.AliWebPayAppPrivateKey.get();
//	public static final String APP_SELLERID = RawStringConfigKey.AliWebPaySellerId.get();
	public static final String ALIPAY_PUBLIC_KEY = RawStringConfigKey.AliWebPayPublicKey.get();
	
	public static final String FORMAT = "json";
	public static final String CHARSET = "UTF-8";
	public static final String ALIPAY_GATEWAY = "https://openapi.alipay.com/gateway.do";
	public static final String SIGN_TYPE = "RSA2";
	
	public static final String NOTIFY_URL = InProduction.get() ? "http://www.ddlad.com/pay/ali/notify" : 
		"http://web.z.ziduan.com/pay/ali/notify";
	
	public static final String RETURN_URL = InProduction.get() ? "http://www.ddlad.com/pages/admin/order.html" :
		"http://web.z.ziduan.com/pages/admin/order.html";
	
	
	
	public static final String LOGGER_NAME = "alipay";
}
