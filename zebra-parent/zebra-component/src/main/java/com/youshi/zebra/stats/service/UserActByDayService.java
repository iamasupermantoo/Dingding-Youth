package com.youshi.zebra.stats.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.service.AbstractService;
import com.youshi.zebra.stats.dao.UserActByDayDAO;
import com.youshi.zebra.stats.model.UserActByDayModel;


/**
 * 
 * 
 * @author codegen
 * 
 */
@Service
public class UserActByDayService extends AbstractService<Integer, UserActByDayModel>{
	private static final Logger logger = LoggerFactory.getLogger(UserActByDayService.class);
	
	@Autowired
	private UserActByDayDAO userActByDayDAO;

	public void create(Integer normalUserCount, Integer payUserCount, String day) {
		userActByDayDAO.insert(normalUserCount, payUserCount, day, System.currentTimeMillis());
		
		logger.info("Create user ACT succ. day: {}, normalUserCount: {}, payUserCount: {}", 
				day, normalUserCount, payUserCount);
	}
	
	@Override
	public AbstractDAO<Integer, UserActByDayModel> dao() {
		return userActByDayDAO;
	}

	public UserActByDayModel getAct(String day) {
		return userActByDayDAO.getByDay(day);
	}
}