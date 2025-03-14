package com.youshi.zebra.config.dao.impl;

/**
 * 用户配置DAO
 * 
 * @author wangsch
 * @date		2016年11月5日
 *
 */
public interface UserConfigDAO {
	/**
	 * 根据key，value，保存一条用户配置
	 * 
	 * @param	userId	用户id
	 * @param key		配置key
	 * @param value		配置value
	 * @return				是否保存成功
	 */
	public boolean save(Integer userId, String key, String value);
	
}
