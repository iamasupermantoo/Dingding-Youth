package com.youshi.zebra.pay.utils;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.youshi.zebra.pay.constants.WeixinPayConstants;

/**
 * Created by boge on 2017/2/28.
 */
public class WxpayOrderStringBuilder {
    private Map<String, String> unifiedParamMap;
    
    private String apiSecret;

    private WxpayOrderStringBuilder(){
        this.unifiedParamMap = new TreeMap<String, String>();
    }

    public static WxpayOrderStringBuilder builder(){
        return new WxpayOrderStringBuilder();
    }

    public WxpayOrderStringBuilder setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
		return this;
	}
    
    public WxpayOrderStringBuilder setAppId(String appId){
        this.unifiedParamMap.put("appid",appId);
        return this;
    }

    public WxpayOrderStringBuilder setMchId(String mchId){
        this.unifiedParamMap.put("mch_id",mchId);
        return this;
    }

    public WxpayOrderStringBuilder setDeviceInfo(String deviceInfo){
        this.unifiedParamMap.put("device_info",deviceInfo);
        return this;
    }

    public WxpayOrderStringBuilder setNonceStr(String nonceStr){
        this.unifiedParamMap.put("nonce_str",nonceStr);
        return this;
    }

    public WxpayOrderStringBuilder setBody(String body){
        this.unifiedParamMap.put("body",body);
        return this;
    }

    public WxpayOrderStringBuilder setDetail(String detail){
        this.unifiedParamMap.put("detail",detail);
        return this;
    }

    public WxpayOrderStringBuilder setAttach(String attach){
        this.unifiedParamMap.put("attach",attach);
        return this;
    }

    public WxpayOrderStringBuilder setOutTradeNo(String outTradeNo){
        this.unifiedParamMap.put("out_trade_no",outTradeNo);
        return this;
    }

    public WxpayOrderStringBuilder setTotalFee(String totalFee){
        this.unifiedParamMap.put("total_fee",totalFee);
        return this;
    }

    public WxpayOrderStringBuilder setSpbillCreateIp(String spbillCreateIp){
        this.unifiedParamMap.put("spbill_create_ip",spbillCreateIp);
        return this;
    }

    public WxpayOrderStringBuilder setNotifyUrl(String notifyUrl){
        this.unifiedParamMap.put("notify_url",notifyUrl);
        return this;
    }

    public WxpayOrderStringBuilder setTradeType(String tradeType){
        this.unifiedParamMap.put("trade_type",tradeType);
        return this;
    }

    public WxpayOrderStringBuilder setSign(String sign){
        this.unifiedParamMap.put("sign",sign);
        return this;
    }

    public WxpayOrderStringBuilder setSignType(String signType){
        this.unifiedParamMap.put("sign_type",signType);
        return this;
    }

    public WxpayOrderStringBuilder setFeeType(String feeType){
        this.unifiedParamMap.put("fee_type",feeType);
        return this;
    }

    public WxpayOrderStringBuilder setTimeStart(String timeStart){
        this.unifiedParamMap.put("time_start",timeStart);
        return this;
    }

    public WxpayOrderStringBuilder setTimeExpire(String timeExpire){
        this.unifiedParamMap.put("time_expire",timeExpire);
        return this;
    }

    public WxpayOrderStringBuilder setGoodsTag(String goodsTag){
        this.unifiedParamMap.put("goods_tag",goodsTag);
        return this;
    }

    public WxpayOrderStringBuilder setLimitPay(String limitPay){
        this.unifiedParamMap.put("limit_pay",limitPay);
        return this;
    }

    /**
     * 构建prepayid
     * 
     * @return	prepayid
     */
    public String buildPrepayid() {
        Map<String, String> restmap = prepayInvoke();

        String prepayid = null;
        if(null != restmap && "SUCCESS".equals(restmap.get("result_code"))){
            prepayid = restmap.get("prepay_id");
        }
        return prepayid;
    }
    
    public String buildCodeUrl() {
    	Map<String, String> restmap = prepayInvoke();
    	
    	String codeUrl = null;
    	if(null != restmap && "SUCCESS".equals(restmap.get("result_code"))){
    		codeUrl = restmap.get("code_url");
    	}
    	return codeUrl;
    }

	/**
	 * @return
	 */
	private Map<String, String> prepayInvoke() {
		if(!this.unifiedParamMap.containsKey("appid")){
            this.setAppId(WeixinPayConstants.APP_ID);
        }

        if(!this.unifiedParamMap.containsKey("mch_id")){
            this.setMchId(WeixinPayConstants.PARTNER_ID);
        }

        if(!this.unifiedParamMap.containsKey("notify_url")){
            this.setNotifyUrl(WeixinPayConstants.NOTIFY_URL);
        }

        if(!this.unifiedParamMap.containsKey("trade_type")){
            this.setTradeType("APP");
        }
        
        if(StringUtils.isEmpty(apiSecret)){
        	apiSecret = WeixinPayConstants.WXPAY_API_SECRET;
        }
        this.setSign(WxpayUtils.getSign(this.unifiedParamMap, apiSecret));

        Map<String, String> restmap = WxpayUtils.restPrePayidXml(WeixinPayConstants.WXPAY_UNIFIEDORDER, this.unifiedParamMap);
		return restmap;
	}
    
    
}
