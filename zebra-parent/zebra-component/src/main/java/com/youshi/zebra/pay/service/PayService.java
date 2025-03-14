package com.youshi.zebra.pay.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.constants.InProduction;
import com.dorado.mvc.reqcontext.Platform;
import com.youshi.zebra.core.constants.config.ListConfigKey;
import com.youshi.zebra.exception.base.DoradoRuntimeException;
import com.youshi.zebra.exception.common.EntityNotNormalException;
import com.youshi.zebra.exception.common.ForbiddenException;
import com.youshi.zebra.order.constants.OrderStatus;
import com.youshi.zebra.order.constants.PayStatus;
import com.youshi.zebra.order.constants.ProductType;
import com.youshi.zebra.order.model.OrderModel;
import com.youshi.zebra.order.service.OrderHandlerService;
import com.youshi.zebra.order.service.OrderService;
import com.youshi.zebra.pay.constants.PayChannel;
import com.youshi.zebra.pay.exception.OrderNotFoundException;
import com.youshi.zebra.pay.model.AlipayParams;
import com.youshi.zebra.pay.model.ApplePayParams;
import com.youshi.zebra.pay.model.PayParams;
import com.youshi.zebra.pay.model.WeixinPayParams;

/**
 * 
 * @author wangsch
 * @date 2017年4月18日
 */
@Service
public class PayService {
	private static final Logger logger = LoggerFactory.getLogger(PayService.class);
	
	@Autowired
	private AlipayService alipayService;
	
	@Autowired
	private WeixinPayService weixinPayService;
	
	@Autowired
	private ApplePayService applePayService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderHandlerService orderHandlerService;
	
	/**
	 * 获取客户端执行支付需要的参数
	 */
	public PayParams getParams(Integer userId, Integer orderId,
			Integer channel, Platform platform) {
		PayChannel payChannel = PayChannel.fromValue(channel);
		PayParams params = params(userId, orderId, payChannel, platform);
		
		return params;
	}
	
	/**
	 * 验证支付结果，更新订单状态、支付状态、做业务处理
	 * 
	 * @param userId
	 * @param channel
	 * @param orderSn
	 * @param tradeId
	 * @return
	 */
	public PayStatus verifyResult(Integer userId, Integer channel, String orderSn, 
			String data, String tradeId) {
		PayChannel payChannel = PayChannel.fromValue(channel);
		// 验证订单状态
		OrderModel order = orderService.getOrderBySn(orderSn);
		if(order == null) {
			logger.error("Order Not found. userId: {}, payChannel: {}, orderSn: {}, tradeId: {}",
					userId, payChannel, orderSn, tradeId);
			throw new OrderNotFoundException();
		}
		if(order.getUserId() != userId) {
			logger.error("This is NOT your order. userId: {}, payChannel: {}, orderSn: {}, tradeId: {}",
					userId, payChannel, orderSn, tradeId);
			throw new ForbiddenException();
		}
		if(order.getStatus() == OrderStatus.FINISHED.getValue()) {
			logger.info("Order already finished. userId: {}, payChannel: {}, orderSn: {}, tradeId: {}",
					userId, payChannel, orderSn, tradeId);
			return PayStatus.PAID;
		}
		
		// 查询支付状态
		PayStatus result = null;
		switch(payChannel) {
			case ALIPAY:
				result = alipayService.queryTrade(orderSn, tradeId);
				break;
			case WEIXIN:
				result = weixinPayService.queryTrade(orderSn, tradeId);
				break;
			case APPLE:
				result = applePayService.verify(orderSn, data);
				
				break;
			default:
				throw new DoradoRuntimeException("Unsupported pay channel: " + payChannel);
		}
		// 只有苹果、微信、支付宝告诉客户端支付成功，客户端才会来二次验证，正常情况，结果应该是已支付
		if(result != PayStatus.PAID) {
			logger.error("Order NOT_PAID(this should NOT happen), userId: {}, payChannel: {}, orderSn: {}, tradeId: {}.", 
					userId, payChannel, orderSn, tradeId);
			return result;
		}
		
		// 做业务处理
		orderHandlerService.paySucc(orderSn, payChannel);
		
		logger.info("Order PAID. userId: {}, payChannel: {}, orderSn: {}, tradeId: {}", 
				userId, payChannel, orderSn, tradeId);
		return result;
	}
	
	
	public boolean dealNotify(HttpServletRequest request, PayChannel payChannel) {
		String orderSn = null;
		switch (payChannel) {
		case ALIPAY:
			orderSn = alipayService.dealNotify(request);
			break;
		case WEIXIN:
			orderSn = weixinPayService.dealNotify(request);
			break;

		default:
			throw new DoradoRuntimeException("Unsupported pay channel: " + payChannel);
		}
		if(orderSn == null) {
			return false;
		}
		
		orderHandlerService.paySucc(orderSn, payChannel);
		
		logger.info("Deal notify succ. payChannel: {}", payChannel);
		
		return true;
	}
	
	private PayParams params(Integer userId, Integer orderId, 
			PayChannel payChannel, Platform platform) {
		
		// 订单
		OrderModel order = orderService.getOrder(orderId);
		if(order == null || order.getStatus() == OrderStatus.FINISHED.getValue()) {
			throw new EntityNotNormalException();
		}
		
		// 价格
		int price = adjustPrice(userId, order.getTotalPrice());
		
		// 获取参数
		int productId = order.getProductId();
		ProductType productType = ProductType.fromValue(order.getProductType());
		
		String orderSn = order.getOrderSn();
		String productName = order.getProductName();
		
		PayParams params = new PayParams();
		params.setOrderSn(orderSn);
		switch(payChannel) {
			case ALIPAY:
				String totalAmount = String.format("%.2f", Integer.valueOf(price).doubleValue() / 100);
			String orderStr = alipayService
						.generateOrderString(orderSn, productName, productName, totalAmount);
				params.setAlipay(new AlipayParams(orderStr));
				break;
				
			case WEIXIN:
				WeixinPayParams weixin = weixinPayService.getParams(orderSn, productName, price);
				params.setWeixin(weixin);
				break;
				
			case APPLE:
				ApplePayParams apple = applePayService.getParams(userId, orderSn, productId, productType);
				params.setApple(apple);
				break;
				
			default:
				throw new DoradoRuntimeException("Unsupported pay channel: " + payChannel);
		}
		return params;
	}

	/**
	 * @param userId
	 * @param price
	 * @return
	 */
	private static int adjustPrice(Integer userId, int price) {
		// 这里打个补丁，测试环境或者测试人员订单金额设置为1分钱
		if(InProduction.get()) {
			List<Object> userIds = ListConfigKey.PAY_TEST_USER_IDS.get();
			for (Object object : userIds) {
				Integer testUserId = (Integer)object;
				if(testUserId.intValue() == userId.intValue()) {
					price = 1;
					logger.info("Pay test userId found: {}, price set to 1.", testUserId);
					break;
				}
			}
		} else {
			price = 1;
		}
		return price;
	}
}
