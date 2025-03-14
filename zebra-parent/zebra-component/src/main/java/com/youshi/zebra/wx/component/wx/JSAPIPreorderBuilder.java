package com.youshi.zebra.wx.component.wx;

import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youshi.zebra.pay.constants.WeixinPayConstants;
import com.youshi.zebra.pay.utils.WxpayUtils;
import com.youshi.zebra.wx.component.wx.constants.WXFWHConstants;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年4月22日
 */
public class JSAPIPreorderBuilder {
	private static final Logger logger = LoggerFactory.getLogger(JSAPIPreorderBuilder.class);
    private Map<String, String> unifiedParamMap;

    private JSAPIPreorderBuilder(){
        this.unifiedParamMap = new TreeMap<String, String>();
    }

    public static JSAPIPreorderBuilder builder(){
        return new JSAPIPreorderBuilder();
    }

    public JSAPIPreorderBuilder setAppId(String appId){
        this.unifiedParamMap.put("appid",appId);
        return this;
    }

    public JSAPIPreorderBuilder setMchId(String mchId){
        this.unifiedParamMap.put("mch_id",mchId);
        return this;
    }

    public JSAPIPreorderBuilder setDeviceInfo(String deviceInfo){
        this.unifiedParamMap.put("device_info",deviceInfo);
        return this;
    }

    public JSAPIPreorderBuilder setNonceStr(String nonceStr){
        this.unifiedParamMap.put("nonce_str",nonceStr);
        return this;
    }

    public JSAPIPreorderBuilder setBody(String body){
        this.unifiedParamMap.put("body",body);
        return this;
    }

    public JSAPIPreorderBuilder setDetail(String detail){
        this.unifiedParamMap.put("detail",detail);
        return this;
    }

    public JSAPIPreorderBuilder setAttach(String attach){
        this.unifiedParamMap.put("attach",attach);
        return this;
    }

    public JSAPIPreorderBuilder setOutTradeNo(String outTradeNo){
        this.unifiedParamMap.put("out_trade_no",outTradeNo);
        return this;
    }

    public JSAPIPreorderBuilder setTotalFee(String totalFee){
        this.unifiedParamMap.put("total_fee",totalFee);
        return this;
    }

    public JSAPIPreorderBuilder setSpbillCreateIp(String spbillCreateIp){
        this.unifiedParamMap.put("spbill_create_ip",spbillCreateIp);
        return this;
    }

    public JSAPIPreorderBuilder setNotifyUrl(String notifyUrl){
        this.unifiedParamMap.put("notify_url",notifyUrl);
        return this;
    }

    public JSAPIPreorderBuilder setTradeType(String tradeType){
        this.unifiedParamMap.put("trade_type",tradeType);
        return this;
    }

	private JSAPIPreorderBuilder setOpenId(String openId) {
		this.unifiedParamMap.put("openid",openId);
        return this;
	}

    public JSAPIPreorderBuilder setSign(String sign){
        this.unifiedParamMap.put("sign",sign);
        return this;
    }

    public JSAPIPreorderBuilder setSignType(String signType){
        this.unifiedParamMap.put("sign_type",signType);
        return this;
    }

    public JSAPIPreorderBuilder setFeeType(String feeType){
        this.unifiedParamMap.put("fee_type",feeType);
        return this;
    }

    public JSAPIPreorderBuilder setTimeStart(String timeStart){
        this.unifiedParamMap.put("time_start",timeStart);
        return this;
    }

    public JSAPIPreorderBuilder setTimeExpire(String timeExpire){
        this.unifiedParamMap.put("time_expire",timeExpire);
        return this;
    }

    public JSAPIPreorderBuilder setGoodsTag(String goodsTag){
        this.unifiedParamMap.put("goods_tag",goodsTag);
        return this;
    }

    public JSAPIPreorderBuilder setLimitPay(String limitPay){
        this.unifiedParamMap.put("limit_pay",limitPay);
        return this;
    }

    /**
     * 构建prepayid
     * 
     * @return	prepayid
     */
    public String buildPrepayid(String openId) {
    	this.setOpenId(openId);
        if(!this.unifiedParamMap.containsKey("appid")){
            this.setAppId(WXFWHConstants.WXPAY_APPID);
        }

        if(!this.unifiedParamMap.containsKey("mch_id")){
            this.setMchId(WXFWHConstants.WXPAY_PARTNER_ID);
        }

        if(!this.unifiedParamMap.containsKey("notify_url")){
            this.setNotifyUrl(WXFWHConstants.WX_PAY_NOTIFY_URL);
        }

        if(!this.unifiedParamMap.containsKey("trade_type")){
            this.setTradeType("JSAPI");
        }

        if(!this.unifiedParamMap.containsKey("sign")){
        	this.setSign(WxpayUtils.getSign(this.unifiedParamMap,WXFWHConstants.WXPAY_API_SECRET));
        }

        Map<String, String> restmap = WxpayUtils.restPrePayidXml(WeixinPayConstants.WXPAY_UNIFIEDORDER, this.unifiedParamMap);
        logger.info("prepayid return: {}", restmap);
        String prepayid = null;
        if(null != restmap && "SUCCESS".equals(restmap.get("result_code"))){
            prepayid = restmap.get("prepay_id");
        }
        return prepayid;
    }
}
