/**
 *@author liwei@longbeach-inc.com
 *@date 2013-10-25
 */
package com.dorado.push.server.listener.impl.push;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.event.EventType;
import com.dorado.framework.event.listener.EventListener;
import com.dorado.push.constants.PushDevice;
import com.dorado.push.constants.PushEventType;
import com.dorado.push.event.PushEvent;
import com.dorado.push.server.utils.PushUtils;
import com.dorado.push.service.PushService;

/**
 * 
 * ios push listener
 * 
 * @author wangsch
 * @date 2016年10月31日
 * 
 * @see AndroidPushListener
 * 
 */
@Service("iosPushListener")
public class IOSPushListener implements EventListener<PushEvent>{

    private Log logger = LogFactory.getLog(getClass());

    @Autowired
    private PushUtils pushUtils;
    
    @Autowired
    private PushService pushNotificationService;

	@Override
	public void deal(PushEvent event) {
		logger.info("ios-push:" + event);
		pushNotificationService.push(EnumSet.of(PushDevice.IOS), event.getUserIds(), pushUtils.buildMessage(event));
	}

	@Override
	public Collection<EventType> listeningTypes() {
		return Arrays.asList(PushEventType.IOSPush);
	}
}
