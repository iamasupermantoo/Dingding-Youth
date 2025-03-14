package com.dorado.push.event;

import java.util.Collection;
import java.util.Map;

import com.dorado.framework.event.EventType;
import com.dorado.push.constants.PushEventType;
import com.dorado.push.event.IPushMessage.PushAction;

public class IOSPushEvent extends AbsPushEvent {
	public IOSPushEvent(Map<String, Object> dataMap) {
		super(dataMap);
	}

	public IOSPushEvent(PushAction type, String message, Collection<Integer> userIds) {
		super(type, message, userIds);
	}
	
	public IOSPushEvent(PushAction type, String message, Collection<Integer> userIds, 
			Integer gotoPage, Map<String, Object> gotoPageParams) {
		super(type, message, userIds, gotoPage, gotoPageParams);
	}
	
	
	@Override
	public EventType getType() {
		return PushEventType.IOSPush;
	}
}
