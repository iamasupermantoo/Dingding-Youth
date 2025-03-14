package com.youshi.zebra.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.HasData;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.crud.service.AbstractService;
import com.dorado.framework.event.utils.PerfUtils;
import com.dorado.framework.model.PageView;
import com.ecyrd.speed4j.StopWatch;
import com.youshi.zebra.core.exception.EntityNotFoundException;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.service.CourseMetaService;
import com.youshi.zebra.order.constants.OrderStatus;
import com.youshi.zebra.order.constants.PayStatus;
import com.youshi.zebra.order.constants.ProductType;
import com.youshi.zebra.order.dao.PreOrderDAO;
import com.youshi.zebra.order.dao.PreOrderDAO.PreOrderStatus;
import com.youshi.zebra.order.model.PreOrderModel;
import com.youshi.zebra.order.utils.OrderUtils;


/**
 * 
 * 
 * @author codegen
 * 
 */
@Service
public class PreOrderService extends AbstractService<Integer, PreOrderModel>{
	private static final Logger logger = LoggerFactory.getLogger(PreOrderService.class);
	
	@Autowired
	private PreOrderDAO preOrderDAO;
	
	@Autowired
	private CourseMetaService courseMetaService;
	

	public void commit(Integer userId, Integer cmId) {
		StopWatch watcher = PerfUtils.getWatcher("PreOrderService.commit");
		CourseMetaModel cm = courseMetaService.getById(cmId);
		if(cm == null) {
			throw new EntityNotFoundException();
		}
		
		long time = System.currentTimeMillis();
		
		int totalPrice = 0;
		int preorderId = preOrderDAO.insert(userId, cmId, ProductType.COURSE.getValue(), totalPrice, 
				time, HasData.EMPTY_DATA, PreOrderStatus.USER_COMMITED, time);
		watcher.stop();
		logger.info("Pre order commit succ. userId: {}, cmId: {}, preorderId: {}", 
				userId, cmId, preorderId);
	}
	
	public PageView<PreOrderModel, HasUuid<Integer>> getPreOrders(String mobile, PreOrderStatus status, Integer cursor, int limit) {
		WhereClause params = WhereClause.create();
		if(status != null) {
			params.and().eq(PreOrderStatus.USER_COMMITED, status.getValue());
		}
		// TODO 根据mobile查询用户id，再查询预约单
		
		PageView<PreOrderModel, HasUuid<Integer>> page = getByCursor(cursor, limit, params);
		
		return page;
	}
	
	@Override
	public AbstractDAO<Integer, PreOrderModel> dao() {
		return preOrderDAO;
	}
}