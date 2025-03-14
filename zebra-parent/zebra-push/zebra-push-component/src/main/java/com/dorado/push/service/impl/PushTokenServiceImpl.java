package com.dorado.push.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dorado.framework.event.utils.PerfUtils;
import com.dorado.push.constants.PushDevice;
import com.dorado.push.dao.impl.AndroidGetuiDeviceTokenDAOImpl;
import com.dorado.push.dao.impl.IOSAPNSDeviceTokenDAOImpl;
import com.ecyrd.speed4j.StopWatch;

@Service
public class PushTokenServiceImpl {
	@Autowired
	@Qualifier("androidGetuiDeviceTokenDAO")
	private AndroidGetuiDeviceTokenDAOImpl androidGetuiDeviceTokenDAO;
	
	@Autowired
	@Qualifier("iosAPNSDeviceTokenDAO")
	private IOSAPNSDeviceTokenDAOImpl iosAPNSDeviceTokenDAO;
	
	public void bindPushToken(Integer userId, PushDevice device, String token) {
		StopWatch watcher = PerfUtils.getWatcher("PushTokenService.bindDeviceToken");
		
		switch (device) {
		case Android:
			androidGetuiDeviceTokenDAO.bind(userId, token);
			break;
		case IOS:
			iosAPNSDeviceTokenDAO.bind(userId, token);
			break;

		default:
			throw new IllegalArgumentException("Unsupported device: " + device);
		}
		
		watcher.stop();
	}
	
	public void unbindPushToken(Integer userId, PushDevice device) {
		StopWatch watcher = PerfUtils.getWatcher("PushTokenService.bindDeviceToken");
		
		switch (device) {
		case Android:
			androidGetuiDeviceTokenDAO.unbind(userId);
			break;
		case IOS:
			iosAPNSDeviceTokenDAO.unbind(userId);
			break;

		default:
			throw new IllegalArgumentException("Unsupported device: " + device);
		}
		
		watcher.stop();
	}
	
}
