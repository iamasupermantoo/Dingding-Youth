package com.dorado.push.dao;

import java.util.Collection;
import java.util.Map;

/**
 * push token和用户id关联
 * 
 * @author wangsch
 * @date 2016年10月31日
 */
public interface DeviceTokenDAO<T> /*extends ScopeAbleDAO<TokenModel, Integer>*/ {

	/**
	 * 绑定token和用户id
	 * 
	 * @param userId	用户id，可以为null（未登录用户）
	 * @param token	设备push token
	 */
	public void bind(Integer userId, T token);
	
	/**
	 * 只有登陆用户可以unbind操作
	 * 
	 * @param 用户id，不为null
	 */
	public void unbind(Integer userId);
	
	/**
	 * 批量获取获取用户的token
	 * 
	 * @param userIds	用户id集合
	 * @return	Map
	 * 						key：用户id，
	 * 						value：用户的token集合，如果允许多设备登陆同一个账号，会返回多个。
	 */
	public Map<Integer, Collection<T>> getTokens(Collection<Integer> userIds);
}
