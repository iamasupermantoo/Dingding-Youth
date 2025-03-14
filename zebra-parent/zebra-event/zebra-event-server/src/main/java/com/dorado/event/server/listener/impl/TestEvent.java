package com.dorado.event.server.listener.impl;

import java.util.Map;

import com.dorado.framework.crud.model.util.ModelUtils;
import com.dorado.framework.event.model.AbsEvent;
import com.google.common.collect.ImmutableMap;
import com.youshi.zebra.event.constant.ZebraEventType;

/**
 * 
 */
public class TestEvent extends AbsEvent {

	public enum SmsEventKey {
		message,
		createTime,
	}
	
    public TestEvent(String message, long createTime) {
        super(ImmutableMap.<String, Object> builder().put(SmsEventKey.message.name(), message)
                .put(SmsEventKey.createTime.name(), createTime).build());
    }

    public TestEvent(Map<String, Object> resolvedData) {
        super(resolvedData);
    }

    @Override
    public ZebraEventType getType() {
        return ZebraEventType.Test;
    }

    public String getMessage() {
        return ModelUtils.getString(this, SmsEventKey.message);
    }
}

