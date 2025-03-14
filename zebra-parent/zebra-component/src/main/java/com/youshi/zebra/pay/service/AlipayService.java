package com.youshi.zebra.pay.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.response.AlipayTradeQueryResponse;
import com.github.phantomthief.util.ObjectMapperUtils;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.core.utils.DateTimeUtils;
import com.youshi.zebra.exception.common.ForbiddenException;
import com.youshi.zebra.order.constants.PayStatus;
import com.youshi.zebra.pay.constants.AlipayConstants;
import com.youshi.zebra.pay.constants.AlipayTradeStatus;
import com.youshi.zebra.pay.constants.PayChannel;
import com.youshi.zebra.pay.dao.PayNotifyDAO;
import com.youshi.zebra.pay.model.AlipayNotify;
import com.youshi.zebra.pay.utils.AlipayApiUtils;
import com.youshi.zebra.pay.utils.AlipayOrderStringBuilder;

/**
 * 
 * @author wangsch
 * @date 2017年2月4日
 */
@Service
public class AlipayService {
	private static final Logger logger = LoggerFactory.getLogger(AlipayService.class);
	
	private static final String TIMEOUT_EXPRESS_1D = "1d";
	
	private static final String VERSION_1_0 = "1.0";
	
	@Autowired
	private PayNotifyDAO payNotifyDAO;
	
	/**
	 * 生成orderString
	 * 
	 */
	public String generateOrderString(String outTradeNo, String bizBody, 
			String bizSubject, String totalAmount) {
		String orderStr = AlipayOrderStringBuilder.builder()
				.appId(AlipayConstants.APP_ID)
				.method("alipay.trade.app.pay")
				.format(AlipayConstants.FORMAT)
				.charset(AlipayConstants.CHARSET)
				.signType(AlipayConstants.SIGN_TYPE)
				.timestamp(DateTimeUtils.getDateTime(System.currentTimeMillis()))
				.version(VERSION_1_0)
				.notifyUrl(AlipayConstants.NOTIFY_URL)
				
				.bizBody(bizBody)
				.bizSubject(bizSubject)
				.bizOutTradeNo(outTradeNo)
				.bizTimeoutExpress(TIMEOUT_EXPRESS_1D)
				.bizTotalAmount(totalAmount)
				.bizSellerId(AlipayConstants.APP_SELLERID)
				.bizProductCode("QUICK_MSECURITY_PAY")
//				.bizGoodsType(goodsType)
//				.bizPassbackParams(passbackParams)
//				.bizPromoParams(promoParams)
//				.bizEnablePayChannels(enablePayChannels)
//				.bizDisablePayChannels(disablePayChannels)
//				.bizStoreId(storeId)
				
//				.extendSysServiceProviderId(sysServiceProviderId)
//				.extendNeedBuyerRealnamed(needBuyerRealnamed)
//				.extendTransMemo(transMemo)
			.build();
		
		return orderStr;
	}
	
	/**
	 * 查询交易
	 */
	public PayStatus queryTrade(String outTradeNo, String tradeNo) {
		AlipayTradeQueryResponse resp = AlipayApiUtils.tradeQuery(outTradeNo, tradeNo);
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
	
	/**
	 * 处理异步通知
	 * 
	 */
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
	
	
	/**
	 * @param request
	 * @return
	 */
	private static AlipayNotify parseNotify(Map<String, String> paramsMap) {
		AlipayNotify notify = new AlipayNotify();
		try {
			BeanUtils.populate(notify, paramsMap);
		} catch (IllegalAccessException | InvocationTargetException e) {
			return null;
		}
		
		return notify;
	}
	
	/**
	 * 关闭交易
	 */
	public void closeTrade() {
	}
	
	/**
	 * 下载对账单
	 */
	public void downBill() {
		
	}
	
	/**
	 * 执行对账
	 */
	public void verifyBill() {
		
	}
	
	public boolean verifyNotify(Map<String, String> paramsMap) {
		boolean succ = AlipayApiUtils.verifyNotify(paramsMap);
//		boolean succ = AlipayApiUtils.verify(paramsMap);
		return succ;
	}
	
}
