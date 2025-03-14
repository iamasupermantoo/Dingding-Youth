package com.youshi.zebra.stats.dao;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.dorado.framework.jedis.JedisByZooKeeper;
import com.dorado.framework.tuple.ThreeTuple;
import com.youshi.zebra.core.constants.jedis.PersistRedisKey;
import com.youshi.zebra.stats.constants.UserStatsConstants;
import com.youshi.zebra.stats.exception.AcqKeyLostException;
import com.youshi.zebra.stats.exception.ActKeyLostException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

/**
 * 用户统计，redis DAO
 * 
 * @author wangsch
 * @date 2017年8月9日
 */
@Repository
public class UserStatsRedisDAO {
	// ----------------------------------------- Notes ---------------------------------------
	/** 
	 * 注意：请保证这个redis，中只有一个shard，因为多个shard不支持sinter操作
	 * 
	 * {@link #computeRetRate(String, String, StatsFlag)}
	 * 
	 */
	private JedisByZooKeeper userStatsRedis = JedisByZooKeeper.of("ustats");
	private JedisCommands jedisClient = userStatsRedis.get();
	
	// ----------------------------------------- END ---------------------------------------
	
	/**
	 * 
	 * 
	 * @author wangsch
	 * @date 2017年8月9日
	 */
	public enum StatsFlag {
		/** acq中代表注册用户，act、ret中代表非付费用户 */
		normal,
		pay,
		ano,
	}
	
	public Integer getAcqUser(String date, StatsFlag flag) {
		String key = acqKey(date, flag);
		return jedisClient.scard(key).intValue();
	}
	
	public Integer getActUser(String date, StatsFlag flag) {
		String key = actKey(date, flag);
		return jedisClient.scard(key).intValue();
	}
	
	public Boolean isAcqUserExist(String date, Integer userId, StatsFlag flag) {
		String key = acqKey(date, flag);
		return jedisClient.sismember(key, String.valueOf(userId));
	}
	
	public void addAcqUser(String date, StatsFlag flag, Integer userId) {
		String key = acqKey(date, flag);
		jedisClient.sadd(key, String.valueOf(userId));
	}
	
	public void addActUser(String date, StatsFlag flag, Integer userId) {
		String key = actKey(date, flag);
		jedisClient.sadd(key, String.valueOf(userId));
	}
	
	public class UserGeneralStatsKey {
		/**
		 * 
		 */
		private static final String USER_CNT = "user_cnt";
		/**
		 * 
		 */
		private static final String PAY_USER_CNT = "pay_user_cnt";
	}
	
	public void addPayUser(Integer userId) {
		jedisClient.sadd(payUsersKey(), String.valueOf(userId));
	}
	
	public void incrPayUser() {
		jedisClient.hincrBy(userGeneralStatsKey(), UserGeneralStatsKey.PAY_USER_CNT, 1);
	}
	
	public void incrUser() {
		jedisClient.hincrBy(userGeneralStatsKey(), UserGeneralStatsKey.USER_CNT, 1);
	}
	
	public boolean isPayUser(Integer userId) {
		return jedisClient.sismember(payUsersKey(), String.valueOf(userId));
	}
	
	/**
	 * 用户相关，总的一些统计信息
	 * 
	 * @author wangsch
	 * @date 2017年8月10日
	 */
	public class UserGeneralStats {
		/** 注册用户数 */
		private Integer userCnt = 0;
		
		/** 付费用户数 */
		private Integer payUserCnt = 0;
		
		public UserGeneralStats(Map<String, String> raw) {
			String rawUserCnt = raw.get(UserGeneralStatsKey.USER_CNT);
			if(StringUtils.isNotEmpty(rawUserCnt)) {
				this.userCnt = Integer.parseInt(rawUserCnt);
			}
			String rawPayCnt = raw.get(UserGeneralStatsKey.PAY_USER_CNT);
			if(StringUtils.isNotEmpty(rawPayCnt)) {
				this.payUserCnt = Integer.parseInt(rawPayCnt);
			}
		}
		
		public Integer getUserCnt() {
			return userCnt;
		}
		
		public Integer getPayUserCnt() {
			return payUserCnt;
		}
	}
	
	public UserGeneralStats getUserGeneralStats(){
		Map<String, String> all = jedisClient.hgetAll(userGeneralStatsKey());
		return new UserGeneralStats(all);
	}
	
	/**
	 * 
	 * 拿某天的注册用户id集合（acq），和某天的活跃用户id集合（act），取两个集合的交集，得到留存的用户id集合（ret）。
	 * 
	 * 计算：
	 * 保留率 = ret用户数 / acq用户数 * 100%
	 * 
	 * @param acqDate	注册日期
	 * @param actDate	活跃日期
	 * @param flag		针对哪种类型用户{@link StatsFlag}
	 * @return			百分比。如：50.5%，返回50.5。结果保留一位小数。计算失败返回{@link UserStatsConstants#NaN}
	 * 
	 * @throws AcqKeyLostException 
	 * @throws ActKeyLostException 
	 */
	public ThreeTuple<Long, Long, String> computeRetRate(String acqDate, String actDate, 
			StatsFlag flag) throws AcqKeyLostException, ActKeyLostException {
		String acqKey = acqKey(acqDate, flag);
		String actKey = actKey(actDate, flag);
		
		try (ShardedJedis shardedJedis = userStatsRedis.getPool().getResource()) {
			Jedis jedis = shardedJedis.getShard(acqKey);
			if(!jedis.exists(acqKey)) {
				throw new AcqKeyLostException(acqKey);
			}
			if(!jedis.exists(actKey)) {
				throw new ActKeyLostException(actKey);
			}
			
			Long acqCount = jedis.scard(acqKey);					// key存在，则这里不会返回0，可以放心使用
			Set<String> retUsers = jedis.sinter(acqKey, actKey);	// 注意：必须在一个redis实力内，才能执行这个命令
			long retCount = retUsers.size();
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.0");
			String rate = df.format((retCount + 0.0) / acqCount * 100);
			
			return new ThreeTuple<Long, Long, String>(retCount, acqCount, rate); 
		}
	}
	
	/**
	 * 采用了key-tags方法，保证acq和act在一个redis实例里边，否则无法进行set的交集
	 * 
	 * @param date
	 * @param flag
	 * @return
	 */
	private String acqKey(String date, StatsFlag flag) {
		String key = PersistRedisKey.UserStats.of("acq-" + flag + "-" + date);
		return key;
	}
	
	private String actKey(String date, StatsFlag flag) {
		String key = PersistRedisKey.UserStats.of("act-" + flag + "-" + date);
		return key;
	}
	
	private String payUsersKey() {
		return PersistRedisKey.UserStats.of("pay-users");
	}
	
	private String userGeneralStatsKey() {
		return PersistRedisKey.UserStats.of("general");
	}
}
