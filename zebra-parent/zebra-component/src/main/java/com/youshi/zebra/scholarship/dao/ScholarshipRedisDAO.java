package com.youshi.zebra.scholarship.dao;

import org.springframework.stereotype.Repository;

import com.dorado.framework.jedis.JedisByZooKeeper;
import com.youshi.zebra.core.constants.jedis.PersistRedisKey;

import redis.clients.jedis.JedisCommands;

/**
 * 
 * 
 * 
 * @author codegen
 */
@Repository
public class ScholarshipRedisDAO {
	private JedisByZooKeeper jedisByZK = JedisByZooKeeper.of("ustats");
	private JedisCommands jedisClient = jedisByZK.get();
	
	public Long addLive(Integer userId, Integer liveMetaId) {
		String key = scholarshipKey(userId);
		return jedisClient.sadd(key, String.valueOf(liveMetaId));
	}
	
	public Long addLesson(Integer userId, Integer courseId, Integer lessonId) {
		String key = scholarshipKey(userId);
		return jedisClient.sadd(key, courseId + "_" + lessonId);
	}
	
	public String scholarshipKey(Integer userId) {
		return PersistRedisKey.SHARE_GET_SCHOLARSHIP.of(userId);
	}
	
}
