package com.youshi.zebra.wx.component.wx.constants;

import com.dorado.framework.constants.InProduction;
import com.youshi.zebra.core.constants.config.RawStringConfigKey;

/**
 * 微信公众号相关, 常量
 * 
 * @author wangsch
 * @date 2017年4月21日
 */
public class WXFWHConstants {
	
	// 公众号账号
	public static String APPID = RawStringConfigKey.WxFwhAppId.get();
	public static String SECRET = RawStringConfigKey.WxFwhSecret.get();
	public static String VERIFY_TOKEN = RawStringConfigKey.WxFwhVerifyToken.get();
	
	// 公众号、扫码支付
	public static final String WXPAY_APPID = RawStringConfigKey.WxFwhAppId.get();;
	public static final String WXPAY_PARTNER_ID = RawStringConfigKey.WxFwhPayPartnerid.get();
	public static final String WXPAY_API_SECRET = RawStringConfigKey.WxFwhPayApiSecret.get();
	
	public static final String WX_PAY_NOTIFY_URL = InProduction.get() 
			? "http://wx.ddlad.com/wxPay/notify" : "http://wx.z.ziduan.com/wxPay/notify";
	
	public static final String WX_WEB_PAY_NOTIFY_URL = InProduction.get() 
			? "http://www.ddlad.com/pay/wx/notify" : "http://web.z.ziduan.com/pay/wx/notify";
	
	public static final String WX_AUTH_REDIRECT_URI = InProduction.get()
			? "http://wx.ddlad.com/auth/callback" : "http://wx.z.ziduan.com/auth/callback";
	
}
