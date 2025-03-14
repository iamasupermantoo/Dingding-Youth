package com.youshi.zebra.stats.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.service.AbstractService;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.stats.dao.UserRetByDayDAO;
import com.youshi.zebra.stats.model.UserRetByDayModel;


/**
 * 
 * 
 * @author codegen
 * 
 */
@Service
public class UserRetByDayService extends AbstractService<Integer, UserRetByDayModel>{
	private static final Logger logger = LoggerFactory.getLogger(UserRetByDayService.class);
	
	@Autowired
	private UserRetByDayDAO userRetByDayDAO;
	
	public void createRetRate(String date, Integer normalUserCount, Integer payUserCount) {
		String empty = "";
		userRetByDayDAO.insert(normalUserCount, payUserCount, 
				empty, empty, date, System.currentTimeMillis());
		
		logger.info("Create RET RATE succ, date: {}, normalUserCount: {}, payUserCount: {}",
				date, normalUserCount, payUserCount);
	}
	
	public void appendRetRate(String date, int afterDay,
			String normalRetRate, String payRetRate) {
		UserRetByDayModel retMode = userRetByDayDAO.getByDay(date);
		if(retMode == null) {
			logger.warn("Append RET RATE IGNORE, no ret record found. date: {}", date);
			return;
		}
		
		String normalUserRets =  retMode.getNormalUserRets() + ", " + afterDay + ":" + normalRetRate;
		String payUserRets = retMode.getPayUserRets() + ", " + afterDay + ":" + payRetRate;
		int c = userRetByDayDAO.update(date, normalUserRets, payUserRets);
		DAOUtils.checkAffectRows(c);
		
		logger.info("Append RET RATE succ, date: {}, afterDay: {}, normalRetRate: {}, payRetRate: {}",
				date, afterDay, normalRetRate, payRetRate);
	}
	
	public UserRetByDayModel getRet(String day) {
		return userRetByDayDAO.getByDay(day);
	}
	
	@Override
	public AbstractDAO<Integer, UserRetByDayModel> dao() {
		return userRetByDayDAO;
	}
}