package com.youshi.zebra.order.service;

import java.util.Map;

import com.youshi.zebra.order.constants.ProductType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.order.constants.ChatResult;
import com.youshi.zebra.order.constants.OrderStatus;
import com.youshi.zebra.order.dao.OrderDAO;
import com.youshi.zebra.order.exception.OrderStatusException;
import com.youshi.zebra.order.model.OrderModel;
import com.youshi.zebra.order.model.OrderModel.OrderKeys;

/**
 * 
 * @author wangsch
 * @date 2017年2月18日
 */
@Service
public class OrderAdminService {
	private Logger logger = LoggerFactory.getLogger(OrderAdminService.class);
	
	@Autowired
	private OrderDAO orderDAO;
	
	@Autowired
	private OrderService orderService;
	
	public PageView<OrderModel, HasUuid<Integer>> queryOrder(Integer userId, OrderStatus status,
			Integer cursor, Integer limit) {
		WhereClause params = WhereClause.create();
		if(status != null) {
			params.and().eq(OrderKeys.order_status, status.getValue());
		} else {
			params.and().notEq(OrderKeys.order_status, OrderStatus.USER_COMMITED);
			params.and().notEq(OrderKeys.order_status, OrderStatus.ADMIN_CONFIRMED);
		}
		if(userId != null) {
			params.and().eq(OrderKeys.user_id, userId);
		}
		// FIXME 暂时只支持COURSE类型
		params.and().eq(OrderKeys.product_type, ProductType.COURSE.getValue());
		
		PageView<OrderModel, HasUuid<Integer>> page = orderService.getByCursor(cursor, limit, params);
		
		return page;
	}
	
	public void confirmOrder(Integer adminId, Integer orderId, Integer price) {
		OrderModel order = orderService.getById(orderId);
		OrderStatus orderStatus = OrderStatus.fromValue(order.getStatus());

		if(orderStatus != OrderStatus.USER_COMMITED) {
			logger.error("Admin confirm FAIL. adminId: {}, orderId: {}, orderSn: {}, status: {}", 
					adminId, order.getId(), order.getOrderSn(), orderStatus);
			throw new OrderStatusException();
		}
		
		price = price * 100;
		int c = orderDAO.updateOrder(orderId, OrderStatus.ADMIN_CONFIRMED, price, System.currentTimeMillis());
		DAOUtils.checkAffectRows(c);
		
		logger.info("Admin confirm order succ. adminId: {}, orderId: {}, orderSn: {}");
	}

	public void chatResult(Integer orderId, Integer result, String remark) {
		OrderModel order = orderService.getById(orderId);
		
		Map<String, Object> dataMap = order.resolvedData();
		dataMap.put(OrderKeys.remark.name(), remark);
		String data = DoradoMapperUtils.toJSON(dataMap);
		
		ChatResult res = ChatResult.fromValue(result);
		int c = orderDAO.updateChatResult(orderId, res, data);
		DAOUtils.checkAffectRows(c);
		logger.info("Admin update chatResult succ. adminId: {}, orderId: {}, orderSn: {}");
	}
}
