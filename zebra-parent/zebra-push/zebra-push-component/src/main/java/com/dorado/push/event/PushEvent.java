package com.dorado.push.event;

import java.util.Collection;

import com.dorado.framework.event.model.Event;

/**
 * push事件，用于给客户端发送push消息
 * 
 * @author wangsch
 * @date 2016年10月31日
 */
public interface PushEvent extends Event {

    /**
     * 消息内容
     * 
     * @return	字符串，消息内容
     */
    String getMessage();

    /**
     * 目标用户id集合
     * 
     * @return	可以为null
     */
    Collection<Integer> getUserIds();

	/**
	 * 
	 * @return	{@link IPushMessage.PushAction}
	 */
    public IPushMessage.PushAction getPushAction();
    
}
