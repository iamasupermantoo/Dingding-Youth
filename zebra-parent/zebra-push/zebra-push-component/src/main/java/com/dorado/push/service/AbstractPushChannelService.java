package com.dorado.push.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.MapUtils;

import com.dorado.push.dao.BadgeStore;
import com.dorado.push.dao.DeviceTokenDAO;
import com.dorado.push.enhander.MessageEnhancer;
import com.dorado.push.event.IPushMessage;
import com.dorado.push.model.SimpleMessage;
import com.dorado.push.utils.PushUtils;

/**
 * 抽象的push 通道服务
 * 
 * @author wangsch
 * @date 2016年11月1日
 * 
 * @param <T> 发push时，需要的客户端token类型
 * 
 */
public abstract class AbstractPushChannelService<T> implements PushChannelService {
	protected final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

	private MessageEnhancer enhancer;

	private BadgeStore badgeStore;

	protected DeviceTokenDAO<T> deviceTokenDAO;
	
	public abstract void servicePush(Collection<T> tokens, String message);

	public abstract void servicePushAll(String message);

	public abstract boolean badgeInc();
	
	@Override
	public void push(Collection<Integer> users, IPushMessage message) {
		Map<Integer, Collection<T>> tokens = deviceTokenDAO.getTokens(users);
		if (MapUtils.isEmpty(tokens)) {
			logger.error("No tokens found.");
			return;
		}
		
		// 遍历每个用户，每个用户可以有有多个token
		for (Entry<Integer, Collection<T>> entry : tokens.entrySet()) {
			SimpleMessage msg = (SimpleMessage) message;
			// incr Badge	FIXME
			boolean flag = false;
			if (badgeInc() && flag) {
				badgeStore.incBadge(entry.getKey());
			}
			if (enhancer != null) {
				enhancer.enhance(msg);
			}
			
			// build the message
			String finalMessage = PushUtils.buildFinalMessage(entry.getKey(), msg);
			
			// do push
			servicePush(entry.getValue(), finalMessage);
		}
	}

	@Override
	public void push(Integer user, IPushMessage message) {
		push(Collections.singleton(user), message);
	}
	
	// ------------------------------------------------getter/setters ------------------------------------------------
	public MessageEnhancer getEnhancer() {
		return enhancer;
	}

	public void setEnhancer(MessageEnhancer enhancer) {
		this.enhancer = enhancer;
	}

	public BadgeStore getBadgeStore() {
		return badgeStore;
	}

	public void setBadgeStore(BadgeStore badgeStore) {
		this.badgeStore = badgeStore;
	}

	public DeviceTokenDAO<T> getDeviceTokenDAO() {
		return deviceTokenDAO;
	}

	public void setDeviceTokenDAO(DeviceTokenDAO<T> deviceTokenDAO) {
		this.deviceTokenDAO = deviceTokenDAO;
	}

	
}
