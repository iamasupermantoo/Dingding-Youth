package com.youshi.zebra.config.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.event.utils.PerfUtils;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.dorado.push.constants.PushDevice;
import com.dorado.push.service.impl.PushTokenServiceImpl;
import com.dorado.push.utils.PushDeviceUtils;
import com.ecyrd.speed4j.StopWatch;
import com.github.phantomthief.util.ObjectMapperUtils;
import com.youshi.zebra.config.dao.impl.UserConfigDAOImpl;
import com.youshi.zebra.config.model.GlobalConfigModel;
import com.youshi.zebra.config.model.UserConfigModel;
import com.youshi.zebra.exception.base.DoradoRuntimeException;

/**
 * 
 * 配置信息，用户配置、全局配置
 * 
 * @author wangsch
 * @date 2016-10-20
 */
@Service
public class ConfigService {
	private static final String CONFIG_PUSH_KEY = "push";

	private static final Logger logger = LoggerFactory.getLogger(ConfigService.class);
	
	private static final Set<String> CONFIG_KEYS = new HashSet<>(Arrays.asList(CONFIG_PUSH_KEY));
	
	@Autowired
	private UserConfigDAOImpl userConfigDAO;
	
	@Autowired
	private PushTokenServiceImpl pushTokenService;
	
	
	
	/**
	 * 获取用户配置信息
	 * 
	 * @param userId
	 * @param platform
	 * 
	 * @return 
	 * 
	 */
	public UserConfigModel getUserConfig(Integer userId) {
		StopWatch watcher = PerfUtils.getWatcher("ConfigService.getUserConfig");
		
		UserConfigModel result = ensureGetExist(userId);
		
		watcher.stop();
		return result;
	}

	private UserConfigModel ensureGetExist(Integer userId) {
		// FIXME 先从redis拿
		
		UserConfigModel result = userConfigDAO.getByUserId(userId);
		if(result == null) {
			UserConfigModel userConfigModel = new UserConfigModel();
			userConfigModel.setData(ObjectMapperUtils.toJSON(Collections.singletonMap("push", "true")));
			return userConfigModel;
		}
		
		return result;
	}

	/**
	 * @return
	 */
	public GlobalConfigModel getGlobalConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 保存用户配置
	 * 
	 * @param userId		用户id
	 * @param key			key
	 * @param value			value
	 */
	// TODO 根据 appver
	public void saveUserConfig(Integer userId, String key, String value/*, AppVer appVer*/) {
		boolean contains = CONFIG_KEYS.contains(key);
		if(!contains) {
			return;
		}
		
		
		switch (key) {
		case "push":
			try{Boolean.valueOf(value);} catch (Exception e){
				throw new IllegalArgumentException("Value format invalid: " + value + " for key: " + key);
			}
			break;

		default:
			break;
		}
		
		doSave(userId, key, value);
		
		unbindPushIfNeeded(userId, key, value);
	}
	
	
	private void doSave(Integer userId, String key, String value) {
		boolean saveSucc = userConfigDAO.save(userId, key, value);
		if(!saveSucc) {
			logger.error("Save user config fail, userId: {}, key: {}, value: {}", userId, key, value);
			throw new DoradoRuntimeException();
		}
	}
	
	// FIXME
	private void unbindPushIfNeeded(Integer userId, String key, Object value) {
		boolean needed = CONFIG_PUSH_KEY.equalsIgnoreCase(key);
		if(needed && value instanceof Boolean) {
			
			Boolean pushOpen = (Boolean)value;
			PushDevice device = PushDeviceUtils.getDevice(WebRequestContext.getAppPlatform());
			if(pushOpen) {
				
				
			} else {
				pushTokenService.unbindPushToken(userId, device);
				logger.info("User UNBIND device token cause USER CONFIG. userId: {}", userId);
			}
			
			
		}
	}
	
	
	
	
}
