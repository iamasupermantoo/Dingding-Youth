package com.youshi.zebra.stats.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.service.AbstractService;
import com.youshi.zebra.stats.dao.UserAcqByDayDAO;
import com.youshi.zebra.stats.model.UserAcqByDayModel;


/**
 * 
 * 
 * @author codegen
 * 
 */
@Service
public class UserAcqByDayService extends AbstractService<Integer, UserAcqByDayModel>{
	private static final Logger logger = LoggerFactory.getLogger(UserAcqByDayService.class);
	
	@Autowired
	private UserAcqByDayDAO userAcqByDayDAO;

	public void create(Integer normalUserCount, Integer payUserCount, String day) {
		userAcqByDayDAO.insert(normalUserCount, payUserCount, day, System.currentTimeMillis());
		
		logger.info("Create user ACQ succ. day: {}, normalUserCount: {}, payUserCount: {}", 
				day, normalUserCount, payUserCount);
	}
	
	public UserAcqByDayModel getAcq(String day) {
		UserAcqByDayModel result = userAcqByDayDAO.getByDay(day);
		return result;
	}
	
	@Override
	public AbstractDAO<Integer, UserAcqByDayModel> dao() {
		return userAcqByDayDAO;
	}
}