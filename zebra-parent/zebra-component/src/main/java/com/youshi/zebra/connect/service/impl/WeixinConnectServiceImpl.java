package com.youshi.zebra.connect.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.youshi.zebra.connect.constant.ConnectType;
import com.youshi.zebra.connect.dao.AccessTokenDAO;
import com.youshi.zebra.connect.dao.ConnectUserDAO;
import com.youshi.zebra.connect.dao.ConnectUserDetailDAO;
import com.youshi.zebra.connect.exception.RemoteServiceErrorException;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年1月10日
 */
@Service("weixinConnectService")
public class WeixinConnectServiceImpl extends AbsConnectService {

	@Autowired
	@Qualifier("weixinConnectUserDAO")
	private ConnectUserDAO connectUserDAO;
	
	@Autowired
	@Qualifier("weixinAccessTokenDAO")
	private AccessTokenDAO accessTokenDAO;
	
	@Autowired
	@Qualifier("weixinConnectUserDetailDAO")
	private ConnectUserDetailDAO connectUserDetailDAO;

	@Override
	protected AccessTokenDAO getAccessTokenDAO() {
		return accessTokenDAO;
	}

	@Override
	protected ConnectUserDAO getConnectUserDAO() {
		return connectUserDAO;
	}

	@Override
	protected ConnectUserDetailDAO getConnectUserDetailDAO() {
		return connectUserDetailDAO;
	}


	@Override
	public String getExternalUserIdByAccessToken(String accessToken,
			int retryTimes) throws RemoteServiceErrorException {
		return null;
	}

	@Override
	public ConnectType getConnectType() {
		return ConnectType.WeiXin;
	}

}
