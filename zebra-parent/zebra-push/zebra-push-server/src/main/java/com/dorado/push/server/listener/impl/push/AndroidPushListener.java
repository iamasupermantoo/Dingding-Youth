package com.dorado.push.server.listener.impl.push;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.event.EventType;
import com.dorado.framework.event.listener.EventListener;
import com.dorado.push.constants.PushDevice;
import com.dorado.push.constants.PushEventType;
import com.dorado.push.event.IPushMessage;
import com.dorado.push.event.PushEvent;
import com.dorado.push.server.utils.PushUtils;
import com.dorado.push.service.PushService;
import com.notnoop.apns.PayloadBuilder;

/**
 * 
 * 安卓 push listener，IOS类似。
 * 
 * 一个事件的转化过程如下：<br />
 * 1. 从队列中，接收到事件对象{@link PushEvent}<br />
 * 2. {@link PushEvent}事件对象转化为{@link IPushMessage}对象<br />
 * 3. 通过{@link PayloadBuilder#build()}转换为json字符串（使用ios apns格式）作为最终结果push给客户端。<br />
 * 
 * @author wangsch
 * @date 2016年10月31日
 * 
 * @see IOSPushListener
 * 
 */
@Service("androidPushListener")
public class AndroidPushListener implements EventListener<PushEvent>{

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PushUtils pushUtils;
    
    @Autowired
    private PushService pushService;

	@Override
	public void deal(PushEvent event) {
		logger.debug("android-push, event: {}", event);
		pushService.push(EnumSet.of(PushDevice.Android), event.getUserIds(), pushUtils.buildMessage(event));
		
	}

	@Override
	public Collection<EventType> listeningTypes() {
		return Arrays.asList(PushEventType.AndroidPush);
	}
}
