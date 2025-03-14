package com.youshi.zebra.order.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.model.HasData;
import com.dorado.framework.crud.model.util.UuidUtils;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.youshi.zebra.account.model.AccountChargeItemModel;
import com.youshi.zebra.account.service.AccountService;
import com.youshi.zebra.course.constants.CourseType;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.service.CourseMetaService;
import com.youshi.zebra.course.service.CourseService;
import com.youshi.zebra.course.service.LiveMetaService;
import com.youshi.zebra.exception.base.DoradoRuntimeException;
import com.youshi.zebra.order.constants.OrderStatus;
import com.youshi.zebra.order.constants.PayStatus;
import com.youshi.zebra.order.constants.ProductType;
import com.youshi.zebra.order.dao.OrderDAO;
import com.youshi.zebra.order.exception.ProductInfoException;
import com.youshi.zebra.order.model.OrderModel;
import com.youshi.zebra.order.model.OrderModel.OrderKeys;
import com.youshi.zebra.order.model.ProductInfo;
import com.youshi.zebra.order.model.ProductPriceModel;
import com.youshi.zebra.order.utils.OrderUtils;
import com.youshi.zebra.pay.constants.PayChannel;
import com.youshi.zebra.pay.dao.PayLogDAO;
import com.youshi.zebra.pay.utils.PayUtils;
import com.youshi.zebra.stats.service.UserStatsService;
import com.youshi.zebra.stats.utils.StatsUtils;

/**
 * 
 * @author wangsch
 * @date 2017年11月8日
 */
@Service
public class OrderHandlerService implements OrderHandler {
	private static final Logger logger = LoggerFactory.getLogger(OrderHandlerService.class);
	
	@Autowired
	private OrderDAO orderDAO;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserStatsService userStatsService;
	
	@Autowired
	private CourseMetaService courseMetaService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private LiveMetaService liveMetaService;
	
	@Autowired
	private PayLogDAO payLogDAO;
	
	@Override
	public ProductInfo getProductInfo(String productUuid, ProductType productType) {
		ProductInfo result = null;
		switch(productType) {
			case COURSE: {
				Integer cmId = UuidUtils.toIntId(CourseMetaModel.class, productUuid);
				ProductPriceModel productPrice = productService.getProductPrice(cmId, productType);
				CourseMetaModel cm = courseMetaService.getById(cmId);
				if(productPrice == null || cm == null) {
					logger.error("Course meta product info not valid. cmId: {}");
					throw new ProductInfoException();
				}
				
				result = new ProductInfo(productPrice.getPrice(), 0,
						cmId, productType.getValue(), cm.getName());
				break;
			}
			
			case CHARGE: {
				Integer chargeItemId = UuidUtils.toIntId(AccountChargeItemModel.class, productUuid);
				AccountChargeItemModel item = accountService.getChargeItem(chargeItemId);
				ProductPriceModel productPrice = productService.getProductPrice(chargeItemId, productType);
				if(productPrice == null || item == null) {
					logger.error("Charge item product info not valid. cmId: {}");
					throw new ProductInfoException();
				}
				
				Integer plusAmount = item.getPlusAmount();
				result = new ProductInfo(productPrice.getPrice(), plusAmount, 
						chargeItemId, productType.getValue(), "余额充值"); 
				break;
			}
			default:
				throw new IllegalArgumentException("Unknown product type: " + productType);
		}
		return result;
		
	}

	@Override
	public void paySucc(String orderSn, PayChannel payChannel) {
		OrderModel order = orderService.getOrderBySn(orderSn);
		// TODO 状态验证
		
		// 状态变更
		long currTime = System.currentTimeMillis();	// 当前时间
		payLogDAO.update(orderSn, PayStatus.PAID, payChannel, HasData.EMPTY_DATA, HasData.EMPTY_DATA, currTime);
		orderService.updateStatus(order.getId(), OrderStatus.FINISHED, PayStatus.PAID, payChannel, currTime);
		
		int userId = order.getUserId();
		Integer productId = order.getProductId();
		
		ProductType productType = ProductType.fromValue(order.getProductType());
		switch(productType) {
			case COURSE:
				courseService.create(order.getProductId(), order.getUserId(), CourseType.Normal);
				logger.info("Add user course OK. userId: {}, cmId: {}", userId, productId);
				break;
			case CHARGE:
				int chargeAmount = order.getChargeAmount();
				accountService.charge(userId, chargeAmount);
				
				logger.info("Add user charge OK. userId: {}, chargeItemId: {}, chargeAmount: {}", userId, productId, chargeAmount);
				break;
				
			default:
				throw new DoradoRuntimeException("Unsupported product type: " + productType);
		}
		
		// 加入到付费用户id集合
		// 去除支付测试，白名中的用户
		boolean inWhite = PayUtils.inWhite(userId);
		if(inWhite) {
			logger.info("Ignore pay test user. userId: {}", userId);
			return;
		}
		userStatsService.dealAcqPayUser(StatsUtils.today(), userId);
		// END
	}

	@Override
	public Integer submit(Integer userId, String productUuid, Integer type) {
		long time = System.currentTimeMillis();
		String orderSn = OrderUtils.genOrderSn(time);
		
		ProductType productType = ProductType.fromValue(type);
		ProductInfo productInfo = getProductInfo(productUuid, productType);
		
		Integer productId = productInfo.getProductId();
		String productName = productInfo.getProductName();
		String productDesc = productInfo.getProductDesc();
		
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put(OrderKeys.product_name.name(), productName);
		dataMap.put(OrderKeys.product_desc.name(), productDesc);
		
		int chargeAmount = 0;
		if(productType == ProductType.CHARGE) {
			chargeAmount = productInfo.getAmount();
		}
		
		String data = DoradoMapperUtils.toJSON(dataMap);
		int orderId = orderDAO.insert(userId, productId, productType, orderSn, 
				OrderStatus.USER_SUBMITTED, PayStatus.NOT_PAID, productInfo.getPrice(), chargeAmount, data, time);
		
		return orderId;
	}
	
	@Override
	public void costSucc(Integer userId, String productUuid, Integer type) {
		ProductType productType = ProductType.fromValue(type);
		Integer lmId = ProductService.parseProductId(productUuid, productType);
		
		switch (productType) {
		case OPEN_COURSE:
			liveMetaService.addLive(userId, lmId);
			break;
			
		default:
			throw new IllegalArgumentException();
		}
	}
}
