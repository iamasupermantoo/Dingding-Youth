package com.youshi.zebra.live.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.dorado.framework.jedis.JedisByZooKeeper;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.youshi.zebra.core.constants.jedis.PersistRedisKey;
import com.youshi.zebra.live.constants.RoomInfoField;
import com.youshi.zebra.live.exception.RoomInfoFieldInvalidException;
import com.youshi.zebra.live.model.RoomInfoModel;

import redis.clients.jedis.JedisCommands;

/**
 * 
 * 直播课房间实时信息Redis实现，格式是：以agora的channelName为key，hash结构。
 * 
 * @author wangsch
 * @date 2017年3月14日
 */
@Repository
public class LiveRoomInfoRedisDAO {
	private static final Logger logger = LoggerFactory.getLogger(LiveRoomInfoRedisDAO.class)
			;
	
	private JedisByZooKeeper jedis = JedisByZooKeeper.of("live");
    private JedisCommands jedisCmd = jedis.get();
    
    public Boolean hasField(String channel, String field) {
    	String redisKey = redisKey(channel);
    	Boolean result = jedisCmd.hexists(redisKey, field);
    	return result;
    }
    
    /**
     * 要求字段有指定的值
     * 
     * @param channel
     * @param field
     * @param value
     * @return
     * @throws RoomInfoFieldInvalidException
     */
    public String checkField(String channel, String field, String value) 
    		throws RoomInfoFieldInvalidException {
    	String fieldValue = getField(channel, field);
    	if(!fieldValue.equals(value.toString())) {
    		logger.error("Check field FAIL. channel: {}, field: {}, required: {}, actual: {}", 
    				channel, field, value, fieldValue);
    		throw new RoomInfoFieldInvalidException();
    	}
    	return fieldValue;
    }
    
    /**
     * 要求字段是空的,null或者空字符串
     * 
     * @param channel
     * @param field
     * @return
     * 
     * @throws RoomInfoFieldInvalidException 
     */
    public String checkEmpty(String channel, String field) 
    		throws RoomInfoFieldInvalidException {
    	String fieldValue = getField(channel, field);
    	if(StringUtils.isNotEmpty(fieldValue)) {
    		logger.error("Check field FAIL. channel: {}, field: {}, required empty, actual: {}", 
    				channel, field, fieldValue);
    		throw new RoomInfoFieldInvalidException();
    	}
    	return fieldValue;
    
    }
    
    public Long setField(String channel, String field, String value) {
    	String redisKey = redisKey(channel);
    	Long ret = jedisCmd.hset(redisKey, field, value);
    	
    	return ret;
    }
    
    public String setFields(String channel, Map<String, String> fields) {
    	String redisKey = redisKey(channel);
    	
    	Map<String, String> fieldsMap = new HashMap<>();
    	for (Entry<String, String> entry : fields.entrySet()) {
    		fieldsMap.put(entry.getKey(), entry.getValue());
		}
    	String ret = jedisCmd.hmset(redisKey, fieldsMap);
    	
    	return ret;
    }
    
    public Long expire(String channel) {
    	String redisKey = redisKey(channel);
    	Long ret = jedisCmd.expire(redisKey, (int)TimeUnit.HOURS.toSeconds(1));
    	return ret;
    }
    
    public String getField(String channel,  String field) {
    	String redisKey = redisKey(channel);
    	return jedisCmd.hget(redisKey, field);
    }
    
    public Map<String, String> getFields(String channel, String... fields) {
    	String redisKey = redisKey(channel);
    	
    	List<String> fieldValues = jedisCmd.hmget(redisKey, fields);
    	Map<String, String> result = new HashMap<>();
    	for(int i=0; i<fields.length; i++) {
    		result.put(fields[i], fieldValues.get(i));
    	}
    	
    	return result;
    }
    
    /**
     * 获取房间信息
     * 
     * @param channel	频道名
     * @return			{@link RoomInfoModel}
     */
    public RoomInfoModel getRoomInfo(String channel) {
    	Map<String, String> rawFields = allFields(channel);
    	
    	RoomInfoModel result = new RoomInfoModel();
    	String studentOnlineStatusStr = rawFields.get(RoomInfoField.STUDENT_ONLINE_STATUS);
    	result.setStudentOnlineStatus(StringUtils.isNotEmpty(studentOnlineStatusStr) 
    			? Integer.parseInt(studentOnlineStatusStr) : null);
    	
    	String teacherOnlineStatusStr = rawFields.get(RoomInfoField.TEACHER_ONLINE_STATUS);
    	result.setTeacherOnlineStatus(StringUtils.isNotEmpty(teacherOnlineStatusStr) 
    			? Integer.parseInt(teacherOnlineStatusStr) : null);
    	
    	result.setFramesInfoJson(rawFields.get(RoomInfoField.FRAMES_INFO_JSON));
    	String currentFrameXStr = rawFields.get(RoomInfoField.CURRENT_FRAME_X);
    	result.setCurrentFrameX(StringUtils.isNotEmpty(currentFrameXStr) 
    			? Integer.parseInt(currentFrameXStr) : null);
    	
    	String currentFrameYStr = rawFields.get(RoomInfoField.CURRENT_FRAME_Y);
    	result.setCurrentFrameY(StringUtils.isNotEmpty(currentFrameYStr) 
    			? Integer.parseInt(currentFrameYStr) : null);
    	
    	String audienceIdListStr = rawFields.get(RoomInfoField.AUDIENCE_LIST);
    	result.setAudienceIdList(StringUtils.isNotEmpty(audienceIdListStr) 
    			? DoradoMapperUtils.fromJSON(audienceIdListStr, ArrayList.class, Integer.class) : null);
    	
    	
    	
    	
    	return result;
    }
    
    
    /**
     * 
     * @param channel
     * @return			房间信息Map，不会返回null
     */
    public Map<String, String> allFields(String channel) {
    	String redisKey = redisKey(channel);
    	Map<String, String> allFields = jedisCmd.hgetAll(redisKey);
    	
    	return allFields == null ? new HashMap<>() : allFields;
    }
    
    private String redisKey(String channel) {
    	return PersistRedisKey.RoomRealtimeInfo.of(channel);
    }
	
}
