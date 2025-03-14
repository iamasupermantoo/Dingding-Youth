package com.dorado.runner.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dorado.framework.utils.DoradoBeanFactory;
import com.youshi.zebra.stats.service.UserStatsService;
import com.youshi.zebra.stats.utils.StatsUtils;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年8月9日
 */
public class UserStatsDayRunner {
	private static final Logger logger = LoggerFactory.getLogger(UserStatsDayRunner.class);
	
	public static void main(String[] args) throws Exception {
		DoradoBeanFactory.init();
		UserStatsService userStatsService = DoradoBeanFactory.getBean(UserStatsService.class);
		
		logger.info("-------------------------------- start UserStatsDayRunner --------------------------------");
		long todayMillis = StatsUtils.todayTimeMills();
		logger.info("Do stats today is: {}", StatsUtils.today());
		try {
			userStatsService.doStats(todayMillis);
		} catch(Exception e) {
			logger.error("Ops doStats", e);
		}
		logger.info("-------------------------------- Finish UserStatsDayRunner --------------------------------");
		System.exit(0);
	}
}
