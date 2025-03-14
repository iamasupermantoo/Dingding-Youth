package com.youshi.zebra.pay.service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.mvc.reqcontext.WebRequestContext;
import com.github.phantomthief.util.ObjectMapperUtils;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.exception.common.ForbiddenException;
import com.youshi.zebra.order.constants.PayStatus;
import com.youshi.zebra.pay.constants.PayChannel;
import com.youshi.zebra.pay.constants.WeixinPayConstants;
import com.youshi.zebra.pay.constants.WxpayTradeState;
import com.youshi.zebra.pay.dao.PayNotifyDAO;
import com.youshi.zebra.pay.model.WeixinPayParams;
import com.youshi.zebra.pay.utils.WxpayOrderStringBuilder;
import com.youshi.zebra.pay.utils.WxpayUtils;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年2月6日
 */
@Service
public class WeixinPayService {
	private static final Logger logger = LoggerFactory.getLogger(WeixinPayService.class);
	
	@Autowired
	private PayNotifyDAO payNotifyDAO;
	
	public WeixinPayParams getParams(String orderSn, String body, int totalPrice) {
		String noncestr = RandomStringUtils.randomAlphanumeric(32);
		String prepayid = WxpayOrderStringBuilder.builder()
				.setBody(body)
				.setNonceStr(noncestr)
				.setOutTradeNo(orderSn)
				.setTotalFee(String.valueOf(totalPrice))
				.setSpbillCreateIp(WebRequestContext.getCurrentIpInString())
				.buildPrepayid();
		
		Map<String, String> payMap = new HashMap<>();
		payMap.put("appid", WeixinPayConstants.APP_ID);
		payMap.put("partnerid", WeixinPayConstants.PARTNER_ID);
		payMap.put("prepayid", prepayid);
		payMap.put("package", "Sign=WXPay");
		payMap.put("noncestr", noncestr);
		payMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		String sign = WxpayUtils.getSign(payMap, WeixinPayConstants.WXPAY_API_SECRET);
		
		WeixinPayParams params = new WeixinPayParams();
		try {
			BeanUtils.populate(params, payMap);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("Weixin pay param populate FAIL. orderSn: {}", e);
			return null;
		}
		params.setPackageStr("Sign=WXPay");
		params.setSign(sign);
		
		return params;
	}
	
	public PayStatus queryTrade(String orderSn, String tradeId) {
		return queryTrade(orderSn, tradeId, 
				WeixinPayConstants.APP_ID,  WeixinPayConstants.PARTNER_ID, WeixinPayConstants.WXPAY_API_SECRET);
	}
	
	public PayStatus queryTrade(String orderSn, String tradeId, String appId, String partnerId, String apiSecret) {
		WxpayTradeState tradeState = WxpayUtils.orderPayQuery(orderSn, tradeId, appId, partnerId, apiSecret);
		if(tradeState == WxpayTradeState.SUCCESS) {
			return PayStatus.PAID;
		}
		return PayStatus.NOT_PAID;
	}
	
	public String dealNotify(HttpServletRequest request) {
		return dealNotify(request);
	}
	
	public String dealNotify(HttpServletRequest request, String apiSecret) {
		Map<String, String> paramsMap = WxpayUtils.getXMLParamsFromRequest(request);
		boolean verifyOk = WxpayUtils.verifyNotify(paramsMap, apiSecret);
		if(!verifyOk) {
			logger.error("Verify alipay notify fail. ");
			throw new ForbiddenException();
		}
		
		String orderSn = paramsMap.get("out_trade_no");
		
		// 过滤重复
//		PayNotifyModel payNotify = payNotifyDAO.getByNotifyId(notify.getNotify_id());
		
		long time = System.currentTimeMillis();
		int id = payNotifyDAO.insert(orderSn, PayChannel.WEIXIN, ObjectMapperUtils.toJSON(paramsMap), time);
		DAOUtils.checkInsert(id);
		
		String resultCode = paramsMap.get("result_code");
		// SUCCESS/FAIL
		if("SUCCESS".equals(resultCode)) {
			return orderSn;
		} 
		return null;
	}

}
