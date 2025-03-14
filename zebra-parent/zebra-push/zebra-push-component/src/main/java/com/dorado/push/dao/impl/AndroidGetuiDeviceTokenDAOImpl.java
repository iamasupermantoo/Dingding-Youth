package com.dorado.push.dao.impl;

import org.springframework.stereotype.Repository;

import com.dorado.push.dao.AbstractDeviceTokenDAO;

/**
 * 安卓个推，客户端token记录存储
 * 
 * @author wangsch
 * @date 2016年10月31日
 */
@Repository("androidGetuiDeviceTokenDAO")
public class AndroidGetuiDeviceTokenDAOImpl extends AbstractDeviceTokenDAO {

	@Override
	public String getLoginTableName() {
		return "getui_push_token";
	}

	@Override
	public String getUnloginTableName() {
		return "getui_unloginpush_token";
	}
	
	
	
	
}
