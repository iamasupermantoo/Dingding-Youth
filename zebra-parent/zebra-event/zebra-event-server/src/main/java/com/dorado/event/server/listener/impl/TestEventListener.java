package com.dorado.event.server.listener.impl;

import java.util.Collection;
import java.util.Collections;

import org.springframework.stereotype.Component;

import com.dorado.framework.event.EventType;
import com.dorado.framework.event.listener.EventListener;
import com.youshi.zebra.event.constant.ZebraEventType;

/**
 * 
 * @author wangsch
 * @date 2017年2月1日
 */
@Component
public class TestEventListener implements EventListener<TestEvent> {
	private int count;
	
	@Override
	public void deal(TestEvent event) {
		if(count ++ %1000 == 0) {
			System.out.println("Deal event count");
		}
	}

	@Override
	public Collection<EventType> listeningTypes() {
		return Collections.singletonList(ZebraEventType.Test);
	}

}
