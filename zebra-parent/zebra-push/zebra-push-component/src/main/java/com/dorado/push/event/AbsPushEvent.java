package com.dorado.push.event;

import java.util.Collection;
import java.util.Map;

import com.dorado.framework.event.model.AbsEvent;
import com.dorado.push.event.IPushMessage.PushAction;
import com.google.common.collect.ImmutableMap;


/**
 * 
 * 抽象的push事件
 * 
 * @author wangsch
 * @date 2016年10月31日
 */
public abstract class AbsPushEvent extends AbsEvent implements PushEvent {
	public enum PushEventKeys {
		action,
		message,
		users,
		gotoPage,
		gotoPageParams,
		
	}
	
	public AbsPushEvent(PushAction action, String message, Collection<Integer> userIds) {
		this(ImmutableMap.<String, Object>builder()
				.put(PushEventKeys.message.toString(), message)
				.put(PushEventKeys.action.toString(), action)
				.put(PushEventKeys.users.toString(), userIds)
				.build());
	}
	
	public AbsPushEvent(PushAction type, String message, Collection<Integer> userIds, int gotoPage, Map<String, Object> params) {
		this(ImmutableMap.<String, Object>builder()
				.put(PushEventKeys.message.toString(), message)
				.put(PushEventKeys.action.toString(), type)
				.put(PushEventKeys.users.toString(), userIds)
				.put(PushEventKeys.gotoPage.toString(), gotoPage)
				.put(PushEventKeys.gotoPageParams.toString(), params)
				.build());
	}
	
	
    public AbsPushEvent(Map<String, Object> resolvedData) {
		super(resolvedData);
	}

    // -----------------------------------getters-----------------------------------
    @Override
    public IPushMessage.PushAction getPushAction() {
        return PushAction.valueOf((String)resolvedData().get(PushEventKeys.action.toString()));
    }

    // FIXME 通过构造方法射入
    public boolean incrBadge() {
    	return false;
    }
    
    @Override
    public String getMessage() {
        return (String)resolvedData().get(PushEventKeys.message.toString());
    }

	@Override
	@SuppressWarnings("unchecked")
    public Collection<Integer> getUserIds() {
        return (Collection<Integer>)resolvedData().get(PushEventKeys.users.toString());
    }

	public Integer getGotoPage() {
		return (Integer)resolvedData().get(PushEventKeys.gotoPage.toString());
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getGotoPageParams() {
		return (Map<String, Object>)resolvedData().get(PushEventKeys.gotoPageParams.toString());
	}
}
