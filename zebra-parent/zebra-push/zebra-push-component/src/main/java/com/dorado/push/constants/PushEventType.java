package com.dorado.push.constants;

import com.dorado.framework.event.EventType;
/**
 * 
 * push类型
 * 
 * @author wangsch
 * @date 2016-10-31
 */
public enum PushEventType implements EventType {
	AndroidPush(1),
	IOSPush(2),
    ;

	private int value;
	
	PushEventType(int value) {	
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see com.dorado.event.EventType#getValue()
	 */
	@Override
	public int getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see com.dorado.event.EventType#getName()
	 */
	@Override
	public String getName() {
		return this.name();
	}


}
