package com.youshi.zebra.connect.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.youshi.zebra.connect.constant.ConnectType;
import com.youshi.zebra.connect.dao.AccessTokenDAO;
import com.youshi.zebra.connect.dao.ConnectUserDAO;
import com.youshi.zebra.connect.dao.ConnectUserDetailDAO;
import com.youshi.zebra.connect.exception.RemoteServiceErrorException;
import com.youshi.zebra.connect.model.ConnectUserDetail;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年1月10日
 */
@Service("qqConnectService")
public class QQConnectServiceImpl extends AbsConnectService {

	@Autowired
	@Qualifier("qqConnectUserDAO")
	private ConnectUserDAO connectUserDAO;
	
	@Autowired
	@Qualifier("qqAccessTokenDAO")
	private AccessTokenDAO accessTokenDAO;
	
	@Autowired
	@Qualifier("qqConnectUserDetailDAO")
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
		return ConnectType.QQ;
	}

	
	public class QQConnectUserDetail extends ConnectUserDetail {

		public QQConnectUserDetail(String externalUserId, String data, long createTime) {
			super(externalUserId, data, createTime);
		}
		
	}

}
