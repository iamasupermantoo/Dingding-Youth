package com.dorado.push.client;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dorado.framework.event.EventDispatcher;
import com.dorado.push.constants.PushEventType;
import com.dorado.push.event.AndroidPushEvent;
import com.dorado.push.event.IOSPushEvent;
import com.dorado.push.event.IPushMessage.PushAction;
import com.dorado.push.event.PushEvent;

/**
 * 
 * push统一入口，客户端。先将事件入队列，会有push服务去执行push事件。
 * 
 * @author wangsch
 * @date 2016年10月31日
 */
@Component("pushClient")
public class PushClient {
	@Autowired
	private EventDispatcher eventDispatcher;
	
	private static final Set<PushEventType> ALL_EVENT_TYPES = EnumSet.<PushEventType>of(
			PushEventType.AndroidPush, PushEventType.IOSPush);
	
	/**
	 * 给单个用户发push
	 * 
	 * @param toUserId			用户id
	 * @param pushAction		{@link PushAction}
	 * @param message			push消息内容
	 */
	public void pushFire(Integer toUserId, PushAction pushAction, String message) {
		pushEnqueue(pushAction, message, Collections.singletonList(toUserId), ALL_EVENT_TYPES);
	}
	
	/**
	 * 给单个用户发push，gotoPage类型
	 * 
	 * @param toUserId			用户id
	 * @param message			push消息内容
	 * @param gotoPage			gotoPage的值
	 * @param param				gotoPage参数
	 */
	public void pushFireGoto(Integer toUserId, String message, int gotoPage, Map<String, Object> param) {
		pushGotoPageEnqueue(message, gotoPage, param, Collections.singletonList(toUserId), ALL_EVENT_TYPES);
	}
	
	/**
	 * 
	 * 
	 * @see #pushFireGoto(Integer, String, int, Map)
	 * 
	 */
	public void pushFireGotos(List<Integer> toUserIds, String message, int gotoPage, Map<String, Object> param) {
		if(CollectionUtils.isEmpty(toUserIds)) {
			return;
		}
		for (Integer toUserId : toUserIds) {
			pushFireGoto(toUserId, message, gotoPage, param);
		}
	}
	
	// --------------------------------------------- push private methods ---------------------------------------------
	/**
	 * 普通push消息入队列
	 * 
	 * @param pushAction
	 * @param message
	 * @param userIds
	 * @param eventTypes
	 */
    private void pushEnqueue(PushAction pushAction, String message, Collection<Integer> userIds,
            Set<PushEventType> eventTypes) {
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        PushEvent pushEvent = null;
        for (PushEventType eventType : eventTypes) {
        	switch(eventType) {
        	case AndroidPush:
        		pushEvent = new AndroidPushEvent(pushAction, message, userIds);
        		break;
        	case IOSPush:
        		pushEvent = new IOSPushEvent(pushAction, message, userIds);
        		break;
    		default:
    			continue;
        	}
        	
        	eventDispatcher.fire(pushEvent);
        }
    }
    
    /**
     * gotopage类型的push，入队列
     * 
     * @param message
     * @param gotoPage
     * @param param
     * @param userIds
     * @param pushTypes
     */
    private void pushGotoPageEnqueue(String message, int gotoPage, Map<String, Object> param,
            Collection<Integer> userIds, Set<PushEventType> pushTypes) {
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        PushEvent pushEvent = null;
        for (PushEventType pushType : pushTypes) {
        	switch(pushType) {
        	case AndroidPush:
        		pushEvent = new AndroidPushEvent(PushAction.GotoPage, message, userIds, gotoPage, param);
        		break;
        	case IOSPush:
        		pushEvent = new IOSPushEvent(PushAction.GotoPage, message, userIds, gotoPage, param);
        		break;
    		default:
    			continue;
        	}
        	
        	eventDispatcher.fire(pushEvent);
        }
    }
}
