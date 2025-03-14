package com.youshi.zebra.pay.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.github.phantomthief.util.ObjectMapperUtils;
import com.youshi.zebra.pay.constants.AlipayConstants;

/**
 * 
 * 客户端唤起支付宝，需要{@code orderString}参数，多个key=value的形式，如：“key=value&key2=value2”的形式，这个类包含了构造orderString的逻辑。
 * 
 * 参考：
 * <a href="https://doc.open.alipay.com/doc2/detail.htm?treeId=204&articleId=105465&docType=1#kzcs">App支付请求参数说明</a>
 * <a href="https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.9UjgsV&treeId=204&articleId=105299&docType=1">IOS调用说明</a>
 * 
 * @author wangsch
 * @date 2017年2月4日
 */
public class AlipayOrderStringBuilder {
	private static final Logger logger = LoggerFactory.getLogger(AlipayConstants.LOGGER_NAME);
	
	private Map<String, String> commonParamMap;
	
	private Map<String, String> bizContentParamMap;
	
	private Map<String, String> extendParamsMap;
	
	private AlipayOrderStringBuilder() {
		this.commonParamMap = new TreeMap<>();
		this.bizContentParamMap = new TreeMap<>();
		this.extendParamsMap = new TreeMap<>();
	}
	
	public static AlipayOrderStringBuilder builder() {
		return new AlipayOrderStringBuilder();
	}
	
	// ----------------------------------------------公共参数----------------------------------------------
	public AlipayOrderStringBuilder appId(String appId) {
		commonParamMap.put("app_id", appId);
		return this;
	}
	
	public AlipayOrderStringBuilder method(String method) {
		commonParamMap.put("method", method);
		return this;
	}
	
	public AlipayOrderStringBuilder format(String format) {
		commonParamMap.put("format", format);
		return this;
	}
	
	public AlipayOrderStringBuilder charset(String charset) {
		commonParamMap.put("charset", charset);
		return this;
	}
	public AlipayOrderStringBuilder signType(String sign_type) {
		commonParamMap.put("sign_type", sign_type);
		return this;
	}
	
	public AlipayOrderStringBuilder timestamp(String timestamp) {
		commonParamMap.put("timestamp", timestamp);
		return this;
	}
	
	public AlipayOrderStringBuilder version(String version) {
		commonParamMap.put("version", version);
		return this;
	}
	public AlipayOrderStringBuilder notifyUrl(String notifyUrl) {
		commonParamMap.put("notify_url", notifyUrl);
		return this;
	}
	// END 公共参数
	
	// ----------------------------------------------业务参数----------------------------------------------
	public AlipayOrderStringBuilder bizBody(String body) {
		bizContentParamMap.put("body", body);
		return this;
	}
	
	public AlipayOrderStringBuilder bizSubject(String subject) {
		bizContentParamMap.put("subject", subject);
		return this;
	}
	
	public AlipayOrderStringBuilder bizOutTradeNo(String outTradeNo) {
		bizContentParamMap.put("out_trade_no", outTradeNo);
		return this;
	}
	
	public AlipayOrderStringBuilder bizTimeoutExpress(String timeoutExpress) {
		bizContentParamMap.put("timeout_express", timeoutExpress);
		return this;
	}
	
	public AlipayOrderStringBuilder bizTotalAmount(String totalAmount) {
		bizContentParamMap.put("total_amount", totalAmount);
		return this;
	}
	
	public AlipayOrderStringBuilder bizSellerId(String sellerId) {
		bizContentParamMap.put("seller_id", sellerId);
		return this;
	}
	
	public AlipayOrderStringBuilder bizProductCode(String productCode) {
		bizContentParamMap.put("product_code", productCode);
		return this;
	}
	
	public AlipayOrderStringBuilder bizGoodsType(String goodsType) {
		bizContentParamMap.put("goods_type", goodsType);
		return this;
	}
	
	public AlipayOrderStringBuilder bizPassbackParams(String passbackParams) {
		bizContentParamMap.put("passback_params", passbackParams);
		return this;
	}
	
	public AlipayOrderStringBuilder bizPromoParams(String promoParams) {
		bizContentParamMap.put("promo_params", promoParams);
		return this;
	}
	
	public AlipayOrderStringBuilder bizEnablePayChannels(String enablePayChannels) {
		bizContentParamMap.put("enable_pay_channels", enablePayChannels);
		return this;
	}
	
	public AlipayOrderStringBuilder bizDisablePayChannels(String disablePayChannels) {
		bizContentParamMap.put("disable_pay_channels", disablePayChannels);
		return this;
	}
	
	public AlipayOrderStringBuilder bizStoreId(String storeId) {
		bizContentParamMap.put("store_id", storeId);
		return this;
	}
	// END 业务参数
	
	// ----------------------------------------------业务扩展参数----------------------------------------------
	public AlipayOrderStringBuilder extendSysServiceProviderId(String sysServiceProviderId) {
		extendParamsMap.put("sys_service_provider_id", sysServiceProviderId);
		return this;
	}
	
	public AlipayOrderStringBuilder extendNeedBuyerRealnamed(String needBuyerRealnamed) {
		extendParamsMap.put("needBuyerRealnamed	", needBuyerRealnamed);
		return this;
	}
	
	public AlipayOrderStringBuilder extendTransMemo(String transMemo) {
		extendParamsMap.put("TRANS_MEMO", transMemo);
		return this;
	}
	// END 业务扩展参数
	
	/**
	 * 生成{@code orderString}
	 * 
	 * @return	{@code orderString}，失败返回null
	 */
	public String build() {
		if(MapUtils.isNotEmpty(extendParamsMap)) {
			bizContentParamMap.put("extend_params", ObjectMapperUtils.toJSON(extendParamsMap));
		}
		if(MapUtils.isNotEmpty(bizContentParamMap)) {
			commonParamMap.put("biz_content", ObjectMapperUtils.toJSON(bizContentParamMap));
		}

		String waitSignParams = AlipaySignature.getSignContent(commonParamMap);
		
		String sign = null;
		try {
			sign = AlipaySignature.rsa256Sign(waitSignParams,
					AlipayConstants.APP_PRIVATE_KEY, AlipayConstants.CHARSET);
		} catch (AlipayApiException e) {
			logger.error("Fail alipay rsa sign", e);
			return null;
		}
		
		commonParamMap.put("sign", sign);

		try {
			for(Entry<String, String> entry : commonParamMap.entrySet()) {
				commonParamMap.put(entry.getKey(), 
						URLEncoder.encode(entry.getValue(), AlipayConstants.CHARSET));
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("Fail alipay url encode", e);
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		for(Entry<String, String> entry : commonParamMap.entrySet()) {
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		sb.deleteCharAt(sb.length() - 1);
		
		return sb.toString();
	}
}
