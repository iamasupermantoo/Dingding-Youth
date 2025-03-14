package com.youshi.zebra.pay.utils;

import java.util.Map;
import java.util.TreeMap;

import com.youshi.zebra.pay.constants.WeixinPayConstants;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年8月30日
 */
public class WxpayQrCodeBuilder {
    private Map<String, String> unifiedParamMap;

    private WxpayQrCodeBuilder(){
        this.unifiedParamMap = new TreeMap<String, String>();
    }

    public static WxpayQrCodeBuilder builder(){
        return new WxpayQrCodeBuilder();
    }

    public WxpayQrCodeBuilder setAppId(String appId){
        this.unifiedParamMap.put("appid",appId);
        return this;
    }

    public WxpayQrCodeBuilder setMchId(String mchId){
        this.unifiedParamMap.put("mch_id",mchId);
        return this;
    }

    public WxpayQrCodeBuilder setDeviceInfo(String deviceInfo){
        this.unifiedParamMap.put("device_info",deviceInfo);
        return this;
    }

    public WxpayQrCodeBuilder setNonceStr(String nonceStr){
        this.unifiedParamMap.put("nonce_str",nonceStr);
        return this;
    }

    public WxpayQrCodeBuilder setBody(String body){
        this.unifiedParamMap.put("body",body);
        return this;
    }

    public WxpayQrCodeBuilder setDetail(String detail){
        this.unifiedParamMap.put("detail",detail);
        return this;
    }

    public WxpayQrCodeBuilder setAttach(String attach){
        this.unifiedParamMap.put("attach",attach);
        return this;
    }

    public WxpayQrCodeBuilder setOutTradeNo(String outTradeNo){
        this.unifiedParamMap.put("out_trade_no",outTradeNo);
        return this;
    }

    public WxpayQrCodeBuilder setTotalFee(String totalFee){
        this.unifiedParamMap.put("total_fee",totalFee);
        return this;
    }

    public WxpayQrCodeBuilder setSpbillCreateIp(String spbillCreateIp){
        this.unifiedParamMap.put("spbill_create_ip",spbillCreateIp);
        return this;
    }

    public WxpayQrCodeBuilder setNotifyUrl(String notifyUrl){
        this.unifiedParamMap.put("notify_url",notifyUrl);
        return this;
    }

    public WxpayQrCodeBuilder setTradeType(String tradeType){
        this.unifiedParamMap.put("trade_type",tradeType);
        return this;
    }

    public WxpayQrCodeBuilder setSign(String sign){
        this.unifiedParamMap.put("sign",sign);
        return this;
    }

    public WxpayQrCodeBuilder setSignType(String signType){
        this.unifiedParamMap.put("sign_type",signType);
        return this;
    }

    public WxpayQrCodeBuilder setFeeType(String feeType){
        this.unifiedParamMap.put("fee_type",feeType);
        return this;
    }

    public WxpayQrCodeBuilder setTimeStart(String timeStart){
        this.unifiedParamMap.put("time_start",timeStart);
        return this;
    }

    public WxpayQrCodeBuilder setTimeExpire(String timeExpire){
        this.unifiedParamMap.put("time_expire",timeExpire);
        return this;
    }

    public WxpayQrCodeBuilder setGoodsTag(String goodsTag){
        this.unifiedParamMap.put("goods_tag",goodsTag);
        return this;
    }

    public WxpayQrCodeBuilder setLimitPay(String limitPay){
        this.unifiedParamMap.put("limit_pay",limitPay);
        return this;
    }

    /**
     * 构建prepayid
     * 
     * @return	prepayid
     */
    public String buildPrepayid() {
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


        if(!this.unifiedParamMap.containsKey("sign")){
        	this.setSign(WxpayUtils.getSign(this.unifiedParamMap,WeixinPayConstants.WXPAY_API_SECRET));
        }

        Map<String, String> restmap = WxpayUtils.restPrePayidXml(WeixinPayConstants.WXPAY_UNIFIEDORDER, this.unifiedParamMap);

        String prepayid = null;
        if(null != restmap && "SUCCESS".equals(restmap.get("result_code"))){
            prepayid = restmap.get("prepay_id");
        }
        return prepayid;
    }
}
