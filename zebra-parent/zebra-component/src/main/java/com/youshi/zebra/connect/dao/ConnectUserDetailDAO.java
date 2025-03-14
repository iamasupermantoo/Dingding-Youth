package com.youshi.zebra.connect.dao;

import com.youshi.zebra.connect.model.ConnectUserDetail;

/**
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
public interface ConnectUserDetailDAO {
	public ConnectUserDetail getConnectUserDetail(String externalUserId);

	public int insertOrUpdateConnectUserDetail(String externalUserId, String data, long time);
}
