package com.dorado.push.dao.impl;

import org.springframework.stereotype.Repository;

import com.dorado.push.dao.AbstractDeviceTokenDAO;

/**
 *IOS APNS，客户端token记录存储
 * 
 * @author wangsch
 * @date 2016年10月31日
 */
@Repository("iosAPNSDeviceTokenDAO")
public class IOSAPNSDeviceTokenDAOImpl extends AbstractDeviceTokenDAO {

	@Override
	public String getLoginTableName() {
		return "apns_push_token";
	}

	@Override
	public String getUnloginTableName() {
		return "apns_unloginpush_token";
	}
	
	
	
	
}
