package com.youshi.zebra.pay.service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.github.phantomthief.util.ObjectMapperUtils;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.exception.common.ForbiddenException;
import com.youshi.zebra.order.constants.PayStatus;
import com.youshi.zebra.pay.constants.AliWebPayConstants;
import com.youshi.zebra.pay.constants.AlipayTradeStatus;
import com.youshi.zebra.pay.constants.PayChannel;
import com.youshi.zebra.pay.dao.PayLogDAO;
import com.youshi.zebra.pay.dao.PayNotifyDAO;
import com.youshi.zebra.pay.model.AlipayNotify;
import com.youshi.zebra.pay.utils.AliWebPayApiUtils;
import com.youshi.zebra.pay.utils.AlipayApiUtils;

/**
 * 
 * 支付宝，网页支付
 * 
 * @author wangsch
 * @date 2017年8月25日
 */
@Service
public class AliwebPayService {
	private static final Logger logger = LoggerFactory.getLogger(AliwebPayService.class);
	
	private AlipayClient alipayClient = AliWebPayApiUtils.getClient();
	
	@Autowired
	private PayLogDAO payLogDAO;
	
	@Autowired
	private PayNotifyDAO payNotifyDAO;
	
	
	public String payRequest(String outTradeNo, String bizBody, 
			String bizSubject, String totalAmount) {
		
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
	    alipayRequest.setReturnUrl(AliWebPayConstants.RETURN_URL);
	    alipayRequest.setNotifyUrl(AliWebPayConstants.NOTIFY_URL);
	    
//	    Map<String, Object> extendsParamsMap = new HashMap<>();
//	    extendsParamsMap.put("sys_service_provider_id", null);	// QUES 系统商编号
	    
	    Map<String, Object> bizContentMap = new HashMap<>();
	    bizContentMap.put("out_trade_no", outTradeNo);
	    bizContentMap.put("product_code", "FAST_INSTANT_TRADE_PAY");
	    bizContentMap.put("total_amount", totalAmount);
	    bizContentMap.put("subject", bizSubject);
	    bizContentMap.put("body", bizBody);
//	    bizContentMap.put("passback_params", null);	// 公用回传参数，异步通知时原样返回，必须UrlEncode编码
//	    bizContentMap.put("extend_params", DoradoMapperUtils.toJSON(extendsParamsMap));	// 业务扩展参数
	    
	    alipayRequest.setBizContent(DoradoMapperUtils.toJSON(bizContentMap));
	    
	    String form="";
	    try {
	        form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
	    } catch (AlipayApiException e) {
	        e.printStackTrace();
	    }
	    
	    return form;
	}

	/**
	 * 查询交易
	 */
	public PayStatus queryTrade(String outTradeNo, String tradeNo) {
		AlipayTradeQueryResponse resp = AliWebPayApiUtils.tradeQuery(outTradeNo, tradeNo);
		logger.info("outTradeNo: {}, resp: {}", outTradeNo, resp);
		if(!resp.isSuccess()) {
			return PayStatus.NOT_PAID;
		}
		
		AlipayTradeStatus status = AlipayTradeStatus.valueOf(resp.getTradeStatus());
		logger.info("trade status: {}, valueof: {}", resp.getTradeStatus(), status);
		if(status == AlipayTradeStatus.TRADE_FINISHED || status == AlipayTradeStatus.TRADE_SUCCESS) {
			return PayStatus.PAID;
		}
		return PayStatus.NOT_PAID;
	}
	
	public String dealNotify(HttpServletRequest request) {

		Map<String, String> paramsMap = AlipayApiUtils.singleParamMap(request);
		logger.info("paramsMap: {}", paramsMap );
		
		// 验证签名
		boolean verifyOk = verifyNotify(paramsMap);
		if(!verifyOk) {
			logger.error("Verify alipay notify fail. ");
			throw new ForbiddenException();
		}
		
		AlipayNotify notify = parseNotify(paramsMap);
		// 过滤重复
//		PayNotifyModel payNotify = payNotifyDAO.getByNotifyId(notify.getNotify_id());
		
		
		// 优先先把支付宝通知信息存下来
		String orderSn = notify.getOut_trade_no();
		long time = System.currentTimeMillis();
		int id = payNotifyDAO.insert(orderSn, PayChannel.ALIPAY, ObjectMapperUtils.toJSON(paramsMap), time);
		DAOUtils.checkInsert(id);
		
		AlipayTradeStatus status = AlipayTradeStatus.valueOf(notify.getTrade_status());
		if(status == AlipayTradeStatus.TRADE_FINISHED 
				|| status == AlipayTradeStatus.TRADE_SUCCESS) {
			return orderSn;
		}
		return null;
		
		// TODO 超时
		// TODO 退款
		
		
		// 过滤重复
		// 根据notify_id过滤重复
		
		// 根据notify_type，switch，处理退款、交易状态
		
			// case notify_type为交易状态
			// 验证订单状态,比较订单状态和支付宝交易状态
			// out_trade_no是商户订单号，查询orderstatus、paystatus
			
			// trade_status是支付宝交易状态
			// 
			// trade_status->PayStatus组合处理：
		    // 成功->成功：走退款
		    // 成功->关闭：忽略
		    // 未支付->成功：更新订单paystatus、orderstatus、pay_log状态、关闭其他渠道交易
		    // 未支付->关闭：更新orderstatus为close_timeout
			
			// case 退款
			// out_biz_no：商户业务ID，主要是退款通知中返回退款申请的流水号
		
		// passback_params是回传参数
	}
	
	public boolean verifyNotify(Map<String, String> paramsMap) {
		boolean succ = AliWebPayApiUtils.verifyNotify(paramsMap);
		return succ;
	}
	
	private static AlipayNotify parseNotify(Map<String, String> paramsMap) {
		AlipayNotify notify = new AlipayNotify();
		try {
			BeanUtils.populate(notify, paramsMap);
		} catch (IllegalAccessException | InvocationTargetException e) {
			return null;
		}
		
		return notify;
	}
	
}
