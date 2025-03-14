package com.youshi.zebra.register.service;

import java.util.List;

import com.youshi.zebra.connect.constant.ConnectType;
import com.youshi.zebra.connect.model.ConnectBindInfo;
import com.youshi.zebra.connect.service.ConnectService;
import com.youshi.zebra.register.constant.ConnectBindStatus;

/**
 * 站外（包括手机）绑定相关
* 
* Date: May 10, 2016
* 
 * @author wangsch
 *
 */
public interface ConnectBindService {
	
    public ConnectBindStatus getBindStatus(String externalUserId, ConnectType connectType);
    
    public ConnectBindStatus getBindStatus(Integer userId, ConnectType connectType);
    
    public void bindConnect(Integer userId, ConnectType connectType, 
    		String accessToken, String refreshToken, String externalUserId);

    public void unbindConnect(Integer userId, ConnectType connectType);
    
    public ConnectService getConnectService(ConnectType type);

	public List<ConnectBindInfo> getBindList(Integer userId, List<ConnectType> types);

}
