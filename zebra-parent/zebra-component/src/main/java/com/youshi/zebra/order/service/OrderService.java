package com.youshi.zebra.order.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.crud.service.AbstractService;
import com.dorado.framework.event.utils.PerfUtils;
import com.dorado.framework.model.PageView;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.ecyrd.speed4j.StopWatch;
import com.youshi.zebra.core.exception.EntityNotFoundException;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.service.CourseMetaService;
import com.youshi.zebra.order.constants.OrderStatus;
import com.youshi.zebra.order.constants.PayStatus;
import com.youshi.zebra.order.constants.ProductType;
import com.youshi.zebra.order.dao.OrderDAO;
import com.youshi.zebra.order.exception.PreOrderAlreadyExistException;
import com.youshi.zebra.order.model.OrderModel;
import com.youshi.zebra.order.model.OrderModel.OrderKeys;
import com.youshi.zebra.order.model.ProductInfo;
import com.youshi.zebra.order.utils.OrderUtils;
import com.youshi.zebra.pay.constants.PayChannel;

/**
 * 
 * @author wangsch
 * @date 2017年2月18日
 */
@Service
public class OrderService extends AbstractService<Integer, OrderModel> {
	private Logger logger = LoggerFactory.getLogger(OrderService.class);
	
	@Autowired
	private OrderDAO orderDAO;
	
	@Autowired
	private CourseMetaService courseMetaService;
	
	@Autowired
	private OrderHandler orderHandlerService;
	
	public OrderModel getOrder(Integer orderId) {
		OrderModel order = getById(orderId);
		return order;
	}
	
	public OrderModel getOrderBySn(String orderSn) {
		OrderModel order = orderDAO.getByOrderSn(orderSn);
		if(order == null) {
			logger.error("Order NOT FOUND. orderSn: {}", orderSn);
			throw new EntityNotFoundException();
		}
		return order;
	}
	
	/**
	 * 提交订单，通用。提交后，等待支付
	 */
	public OrderModel submit(Integer userId, String productUuid, Integer type) {
		StopWatch watcher = PerfUtils.getWatcher("OrderService.commit");
		
		Integer orderId = orderHandlerService.submit(userId, productUuid, type);
		OrderModel order = getById(orderId);
		
		watcher.stop();
		logger.info("Order submit succ. userId: {}, orderSn: {}", 
				userId, order.getOrderSn());
		return order;
	}
	
	/**
	 * 提交预约单，只适用于课程。提交后，等待运营后台确认价格
	 */
	public void commit(Integer userId, Integer cmId) {
		StopWatch watcher = PerfUtils.getWatcher("OrderService.commit");
		CourseMetaModel cm = courseMetaService.getById(cmId);
		if(cm == null) {
			throw new EntityNotFoundException();
		}
		
		// 是否已经约过课
		List<OrderModel> preOrder = orderDAO.getPreOrder(userId, cmId, ProductType.COURSE);
		if(preOrder != null && preOrder.size() != 0) {
			throw new PreOrderAlreadyExistException();
		}
		
		long time = System.currentTimeMillis();
		String orderSn = OrderUtils.genOrderSn(time);
		
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put(OrderKeys.product_name.name(), cm.getName());
		dataMap.put(OrderKeys.product_desc.name(), StringUtils.abbreviate(cm.getDesc(), 30));
		
		String data = DoradoMapperUtils.toJSON(dataMap);
		int orderId = orderDAO.insert(userId, cmId, ProductType.COURSE, orderSn, 
				OrderStatus.USER_COMMITED, PayStatus.NOT_PAID, 0, 0, data, time);
		watcher.stop();
		logger.info("Order commit succ. userId: {}, cmId: {}, orderId: {}", 
				userId, cmId, orderId);
	}
	
	/**
	 * 更新订单状态
	 */
	public void updateStatus(Integer orderId, OrderStatus orderStatus, 
			PayStatus payStatus, PayChannel payChannel, long updateTime) {
		int c = orderDAO.updateStatus(orderId, orderStatus, payStatus, payChannel, updateTime);
		DAOUtils.checkAffectRows(c);
		
		logger.info("Order status updated. orderId: {}, orderStatus: {}, payStatus: {}", 
				orderId, orderStatus, payStatus);
	}
	
	public PageView<OrderModel, HasUuid<Integer>> getOrders(Integer userId, 
			Integer cursor, Integer limit) {
		
		WhereClause params = WhereClause.create()
				.and().eq(OrderKeys.user_id, userId)
				.and().notEq(OrderKeys.order_status, OrderStatus.USER_COMMITED.getValue())
				;
		PageView<OrderModel, HasUuid<Integer>> page = getByCursor(cursor, limit, params);
		
		return page;
	}

	@Override
	protected AbstractDAO<Integer, OrderModel> dao() {
		return orderDAO;
	}
	
}
