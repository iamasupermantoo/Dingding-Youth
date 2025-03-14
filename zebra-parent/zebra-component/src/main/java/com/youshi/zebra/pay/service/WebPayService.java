package com.youshi.zebra.pay.service;

import java.awt.image.BufferedImage;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.constants.InProduction;
import com.youshi.zebra.core.constants.config.ListConfigKey;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.service.CourseMetaService;
import com.youshi.zebra.exception.base.DoradoRuntimeException;
import com.youshi.zebra.exception.common.EntityStatusException;
import com.youshi.zebra.exception.common.ForbiddenException;
import com.youshi.zebra.order.constants.OrderStatus;
import com.youshi.zebra.order.constants.PayStatus;
import com.youshi.zebra.order.model.OrderModel;
import com.youshi.zebra.order.service.OrderHandlerService;
import com.youshi.zebra.order.service.OrderService;
import com.youshi.zebra.pay.constants.PayChannel;
import com.youshi.zebra.pay.exception.OrderNotFoundException;
import com.youshi.zebra.pay.utils.WxpayUtils;
import com.youshi.zebra.wx.component.wx.constants.WXFWHConstants;

/**
 * 
 * 网页支付Service
 * 
 * @author wangsch
 * @date 2017年8月25日
 */
@Service
public class WebPayService {
	private static final Logger logger = LoggerFactory.getLogger(WebPayService.class);
	
	@Autowired
	private AliwebPayService aliwebPayService;
	
	@Autowired
	private WeixinPayService weixinPayService;
	
	@Autowired
	private FWHPayService fwhPayService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderHandlerService orderHandlerService;
	
	@Autowired
	private CourseMetaService courseMetaService;
	
	public String form(Integer userId, String orderSn) {
//		OrderModel order = orderService.getOrderBySn(orderSn);
//		if(order == null || order.getStatus() == OrderStatus.FINISHED.getValue()) {
//			throw new EntityStatusException();
//		}
//		String totalAmount = String.format("%.2f", 
//				Integer.valueOf(order.getTotalPrice()).doubleValue() / 100);
//		
//		CourseMetaModel cm = courseMetaService.getById(order.getProductId());
//		String courseName = cm.getName();
		
		String courseName = "课程名（测试ali网页支付）";
		String totalAmount = "0.01";
		
		String form = aliwebPayService.payRequest(orderSn, courseName, courseName, totalAmount);
		logger.info(form);
		return form;
	}
	
	public BufferedImage getQRCode(Integer userId, String orderSn) {
		OrderModel order = orderService.getOrderBySn(orderSn);
		if(order == null || order.getStatus() == OrderStatus.FINISHED.getValue()) {
			throw new EntityStatusException();
		}
		
		CourseMetaModel cm = courseMetaService.getById(order.getProductId());
		String courseName = cm.getName();
		
		int price = adjustPrice(userId, order.getTotalPrice());
		String qrCodeUrl = fwhPayService.getQRCodeUrl(orderSn, courseName, price);
		
		
		BufferedImage bufferedImage = WxpayUtils.payQRCode(287, 279, qrCodeUrl);
		
		return bufferedImage;
	}
	
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
				result = aliwebPayService.queryTrade(orderSn, tradeId);
				break;
			case WEIXIN:
				result = weixinPayService.queryTrade(orderSn, tradeId, 
						WXFWHConstants.WXPAY_APPID, WXFWHConstants.WXPAY_PARTNER_ID, WXFWHConstants.WXPAY_API_SECRET);
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
		
//		// 做业务处理，注释掉，依赖异步通知
//		payService.dealOrderPaid(orderSn, payChannel);
		
		logger.info("Order PAID. userId: {}, payChannel: {}, orderSn: {}, tradeId: {}", 
				userId, payChannel, orderSn, tradeId);
		return result;
	}
	
	public boolean dealNotify(HttpServletRequest request, PayChannel payChannel) {
		String orderSn = null;
		switch (payChannel) {
		case ALIPAY:
			orderSn = aliwebPayService.dealNotify(request);
			break;
		case WEIXIN:
			orderSn = weixinPayService.dealNotify(request, WXFWHConstants.WXPAY_API_SECRET);
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
