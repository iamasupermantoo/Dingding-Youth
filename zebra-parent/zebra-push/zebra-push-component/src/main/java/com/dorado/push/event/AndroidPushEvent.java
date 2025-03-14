package com.dorado.push.event;

import java.util.Collection;
import java.util.Map;

import com.dorado.framework.event.EventType;
import com.dorado.push.constants.PushEventType;
import com.dorado.push.event.IPushMessage.PushAction;

public class AndroidPushEvent extends AbsPushEvent {
	public AndroidPushEvent(Map<String, Object> dataMap) {
		super(dataMap);
	}

	public AndroidPushEvent(PushAction pushType, String message, Collection<Integer> userIds) {
		super(pushType, message, userIds);
	}
	
	public AndroidPushEvent(PushAction type, String message, Collection<Integer> userIds, 
			Integer gotoPage, Map<String, Object> params) {
		super(type, message, userIds, gotoPage, params);
	}
	
	
	@Override
	public EventType getType() {
		return PushEventType.AndroidPush;
	}
}
