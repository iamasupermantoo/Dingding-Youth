package com.youshi.zebra.event.constant;

import com.dorado.framework.event.EventType;

/**
 * 事件类型
 * 
 * @author wangsch
 * @date 2017年2月1日
 */
public enum ZebraEventType implements EventType {
    Test(1),
    Sms(2),
    ;

	private int value;
	
	ZebraEventType(int value) {	
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
