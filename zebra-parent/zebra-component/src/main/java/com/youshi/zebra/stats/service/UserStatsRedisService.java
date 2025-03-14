package com.youshi.zebra.stats.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.event.utils.PerfUtils;
import com.dorado.framework.tuple.ThreeTuple;
import com.ecyrd.speed4j.StopWatch;
import com.youshi.zebra.stats.constants.UserStatsConstants;
import com.youshi.zebra.stats.dao.UserStatsRedisDAO;
import com.youshi.zebra.stats.dao.UserStatsRedisDAO.StatsFlag;
import com.youshi.zebra.stats.dao.UserStatsRedisDAO.UserGeneralStats;
import com.youshi.zebra.stats.exception.AcqKeyLostException;
import com.youshi.zebra.stats.exception.ActKeyLostException;

/**
 * 
 * 用户统计，redis服务。利用redis执行一些技术、计算等操作
 * 
 * @author wangsch
 * @date 2017年8月9日
 * 
 * {@link UserStatsService}
 */
@Service
public class UserStatsRedisService {
	private static final Logger logger = LoggerFactory.getLogger(UserStatsRedisService.class);
	
	@Autowired
	private UserStatsRedisDAO userStatsRedisDAO;
	
	public void addActUser(String date, Integer userId) {
		// 用户活跃区分标识
		StatsFlag flag = isPayUser(userId) ? StatsFlag.pay : StatsFlag.normal;
    	
		// 当天新增的用户，不计算活跃
		if(isAcqUserExist(date, userId, flag)) {
			return;
		}
		
		addActUser(date, userId, flag);
	}
	
	public Integer getAcqUserCount(String date, StatsFlag flag) {
		return userStatsRedisDAO.getAcqUser(date, flag);
	}
	
	public Integer getActUserCount(String date, StatsFlag flag) {
		return userStatsRedisDAO.getActUser(date, flag);
	}
	
	public Boolean isAcqUserExist(String date, Integer userId, StatsFlag flag) {
		return userStatsRedisDAO.isAcqUserExist(date, userId, flag);
	}
	
	public UserGeneralStats getUserGeneralStats() {
		UserGeneralStats userGeneralStats = userStatsRedisDAO.getUserGeneralStats();
		return userGeneralStats;
	}
	
	/**
	 * 计算保留率
	 */
	public String computeRetRate(String acqDate, String actDate, StatsFlag flag) {
		StopWatch watcher = PerfUtils.getWatcher("UserStatsRedisService.computeRetRate");
		ThreeTuple<Long, Long, String> result;
		try {
			result = userStatsRedisDAO.computeRetRate(acqDate, actDate, flag);
		} catch (AcqKeyLostException | ActKeyLostException e) {
			logger.warn("Compute ret rate FAIL. acq date: " + acqDate + ", act date: " + actDate + ", flag: " + flag);
			return UserStatsConstants.NaN;
		}
		
		logger.info("Compute ret rate succ. acq date: {}, act date: {}, flag: {}, retCount: {}, acqCount: {}, rate: {}", 
				acqDate, actDate, flag, result.first, result.second, result.third);
		watcher.stop();
		return result.third;
	}
	
	public void addAcqUser(String date, Integer userId, StatsFlag flag) {
		userStatsRedisDAO.addAcqUser(date, flag, userId);
		logger.info("Add one acq user succ. date: {}, userId: {}, flag: {}", date, userId, flag);
	}
	
	public void addActUser(String date, Integer userId, StatsFlag flag) {
		userStatsRedisDAO.addActUser(date, flag, userId);
		logger.debug("Add one acq user succ. date: {}, userId: {}, flag: {}", date, userId, flag);
	}
	
	// ------------------------------------------------ 用户与付费用户 ------------------------------------------------------
	public void addPayUser(Integer userId) {
		userStatsRedisDAO.addPayUser(userId);
		logger.info("Add one pay user  succ. userId: {}", userId);
	}
	
	public void incrUser() {
		userStatsRedisDAO.incrUser();
	}
	
	public void incrPayUser(Integer userId) {
		userStatsRedisDAO.incrPayUser();
	}
	
	public boolean isPayUser(Integer userId) {
		return userStatsRedisDAO.isPayUser(userId);
	}
}
