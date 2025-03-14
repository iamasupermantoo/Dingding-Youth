package com.youshi.zebra.stats.service;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.youshi.zebra.core.utils.DateTimeUtils;
import com.youshi.zebra.stats.dao.UserStatsRedisDAO.StatsFlag;
import com.youshi.zebra.stats.model.UserAcqByDayModel;
import com.youshi.zebra.stats.model.UserActByDayModel;
import com.youshi.zebra.stats.model.UserRetByDayModel;

/**
 * 
 * 用户统计服务，主要是将统计数据持久化到数据库中，统计数据，由{@link UserStatsRedisService}来提供。
 * 需要一个定时器，定时将redis数据持久化到数据库中，每次查询统计报表只查询数据库，不需要每次重新计算（实时跑SQL的人，让我发现，直接打死！）
 * 
 * 
 * 英文及简写：
 * acquisition(acq)		用户获取（如：注册、下载）
 * activation(act)		用户活跃
 * retention(ret)		用户留存
 * 
 * @author wangsch
 * @date 2017年8月9日
 * 
 * {@link UserStatsRedisService}
 * 
 */
@Service
public class UserStatsService {
	private static final Logger logger = LoggerFactory.getLogger(UserStatsService.class);
	
	@Autowired
	private UserAcqByDayService userAcqByDayService;
	
	@Autowired
	private UserActByDayService userActByDayService;
	
	@Autowired
	private UserRetByDayService userRetByDayService;
	
	@Autowired
	private UserStatsRedisService userStatsRedisService;
	
	public PageView<UserAcqByDayModel, HasUuid<Integer>> getAcqs(Integer cursor, int limit) {
		return userAcqByDayService.getByCursor(cursor, limit, WhereClause.EMPTY);
	}
	
	public PageView<UserActByDayModel, HasUuid<Integer>> getActs(Integer cursor, int limit) {
		return userActByDayService.getByCursor(cursor, limit, WhereClause.EMPTY);
	}
	
	public PageView<UserRetByDayModel, HasUuid<Integer>> getRets(Integer cursor, int limit) {
		return userRetByDayService.getByCursor(cursor, limit, WhereClause.EMPTY);
	}
	
	public UserAcqByDayModel getAcq(String day) {
		return userAcqByDayService.getAcq(day);
	}
	
	public UserActByDayModel getAct(String day) {
		return userActByDayService.getAct(day);
	}
	
	public UserRetByDayModel getRet(String day) {
		return userRetByDayService.getRet(day);
	}
	
	// ---------------------------------------- 写记录 ------------------------------------------------
	public void addAcq(Integer normalUserCount, Integer payUserCount, String date) {
		userAcqByDayService.create(normalUserCount, payUserCount, date);
	}
	
	public void addAct(Integer normalUserCount, Integer payUserCount, String date) {
		userActByDayService.create(normalUserCount, payUserCount, date);
	}
	
	public void createRetRate(String date, Integer normalUserCount, Integer payUserCount) {
		userRetByDayService.createRetRate(date, normalUserCount, payUserCount);
	}
	
	public void appendRetRate(String date, int afterDay, String normalRetRate, String payRetRate) {
		userRetByDayService.appendRetRate(date, afterDay, normalRetRate, payRetRate);
	}
	
	// ------------------------------------------- 一些便捷方法 ------------------------------------------------------
	
	/**
	 * 处理一个用户注册
	 * 
	 * @param date		日期
	 * @param userId	用户id
	 */
	public void dealAcqNormalUser(String date, Integer userId) {
		userStatsRedisService.addAcqUser(date, userId, StatsFlag.normal);
		userStatsRedisService.incrUser();
		// ...											// 普通用户就不单独存了，量太大
	}
	
	/**
	 * 处理一个用户支付
	 * 
	 * @param date		日期
	 * @param userId	用户id
	 */
	public void dealAcqPayUser(String date, Integer userId) {
		boolean isPayUser = userStatsRedisService.isPayUser(userId);
		if(isPayUser) {
			logger.info("Already is paid user. userId: {}, date: {}", userId, date);
			return;
		}
		userStatsRedisService.addAcqUser(date, userId, StatsFlag.pay);
		userStatsRedisService.incrPayUser(userId);
		userStatsRedisService.addPayUser(userId);		// 支付的用户，单独存一下
	}
	
	/**
	 * 处理一个日活跃
	 * 
	 * @param date		日期
	 * @param userId	用户id
	 */
	public void dealActUser(String date, Integer userId) {
		userStatsRedisService.addActUser(date, userId);
	}
	
	/**
	 * 基于注册、支付、活跃，数据，做一下统计。一般是次日凌晨，跑报表，统计出上一天的数据。
	 * 
	 * @see #dealAcqNormalUser(String, Integer)
	 * @see #dealAcqPayUser(String, Integer)
	 * @see #dealActUser(String, Integer)
	 * 
	 * 
	 * @param todayMillis	执行统计的时刻，时间毫秒值
	 */
	public void doStats(long todayMillis) {
		long yesterdayMillis = todayMillis - TimeUnit.DAYS.toMillis(1);
		String yesterday = DateTimeUtils.getDate(yesterdayMillis);
		
		// 日注册
		Integer acqNormalUserCount = userStatsRedisService.getAcqUserCount(yesterday, StatsFlag.normal);
		Integer acqPayUserCount = userStatsRedisService.getAcqUserCount(yesterday, StatsFlag.pay);
		addAcq(acqNormalUserCount, acqPayUserCount, yesterday);
		logger.info("Step1: ACQ succ for day: {}.", yesterday);
		
		// 日活跃
		Integer actNormalUserCount = userStatsRedisService.getActUserCount(yesterday, StatsFlag.normal);
		Integer actPayUserCount = userStatsRedisService.getActUserCount(yesterday, StatsFlag.pay);
		addAct(actNormalUserCount, actPayUserCount, yesterday);
		logger.info("Step2: ACT succ for day: {}.", yesterday);
		
		// 保留率，范围：1天后 - 28天后
		createRetRate(yesterday, acqNormalUserCount, acqPayUserCount);
		String actDate = yesterday;
		for(int afterDay = 1; afterDay <= 28; afterDay ++) {
			String acqDate = DateTimeUtils.getDate(yesterdayMillis - TimeUnit.DAYS.toMillis(afterDay));
			String normalRetRate = userStatsRedisService.computeRetRate(acqDate, actDate, StatsFlag.normal);
			String payRetRate = userStatsRedisService.computeRetRate(acqDate, actDate, StatsFlag.pay);
			appendRetRate(acqDate, afterDay, normalRetRate, payRetRate);
			
			logger.info("Step3: RET after {} days for acq date {} succ.", afterDay, acqDate);
		}
	}
}
