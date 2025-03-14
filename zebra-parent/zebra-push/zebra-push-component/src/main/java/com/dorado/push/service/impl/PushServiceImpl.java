package com.dorado.push.service.impl;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dorado.push.constants.PushDevice;
import com.dorado.push.event.IPushMessage;
import com.dorado.push.service.PushChannelService;
import com.dorado.push.service.PushService;

/**
 * 
 * 
 * @author wangsch
 * @date 2016年11月1日
 */
@Service
public class PushServiceImpl implements PushService {
//	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private Map<PushDevice, PushChannelService> channels;
	
	@Autowired
	@Qualifier("androidGetuiPushChannel")
	private AndroidGetuiPushChannelService androidGetuiPushChannel;
	
	@Autowired
	@Qualifier("iosAPNSChannel")
	private IOSAPNSChannelService iosAPNSChannel;
	
	@PostConstruct
	public void registerChannels() {
		channels = new HashMap<>();
		channels.put(PushDevice.Android, androidGetuiPushChannel);
		channels.put(PushDevice.IOS, iosAPNSChannel);
		
	}
	
	
	public void setChannels(Map<PushDevice, PushChannelService> channels) {
		this.channels = channels;
	}
	
	@Override
	public void push(EnumSet<PushDevice> pushDevice, Collection<Integer> userIds, IPushMessage message) {
		for (PushDevice device : pushDevice) {
			PushChannelService pushChannelService = channels.get(device);
			pushChannelService.push(userIds, message);
		}
	}

	@Override
	public void pushUnlogin(EnumSet<PushDevice> pushDevice, Collection<String> tokens, IPushMessage message) {
		
	}

}
