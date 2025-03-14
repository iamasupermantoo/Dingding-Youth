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
 * Date: May 10, 2016
 * 
 * @author wangsch
 *
 */
@Service("mobileConnectService")
public class MobileConnectServiceImpl extends AbsConnectService {

	@Autowired
	@Qualifier("mobileConnectUserDAO")
	private ConnectUserDAO mobileConnectUserDAO;
	
	/* (non-Javadoc)
	 * @see com.dorado.connect.service.ConnectService#getExternalUserIdByAccessToken(java.lang.String)
	 */
	@Override
	public String getExternalUserIdByAccessToken(String accessToken)
			throws RemoteServiceErrorException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.dorado.connect.service.ConnectService#getExternalUserIdByAccessToken(java.lang.String, int)
	 */
	@Override
	public String getExternalUserIdByAccessToken(String accessToken,
			int retryTimes) throws RemoteServiceErrorException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.dorado.connect.service.impl.AbsConnectService#getAccessTokenDAO()
	 */
	@Override
	protected AccessTokenDAO getAccessTokenDAO() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.dorado.connect.service.impl.AbsConnectService#getConnectUserDAO()
	 */
	@Override
	protected ConnectUserDAO getConnectUserDAO() {
		return mobileConnectUserDAO;
	}

//	/* (non-Javadoc)
//	 * @see com.dorado.connect.service.impl.AbsConnectService#getExpiredKeyCodec()
//	 */
//	@Override
//	protected BooleanConfigCodec getExpiredKeyCodec() {
//		// TODO Auto-generated method stub
//		return null;
//	}

	/* (non-Javadoc)
	 * @see com.dorado.connect.service.ConnectService#getConnectType()
	 */
	@Override
	public ConnectType getConnectType() {
		return ConnectType.Mobile;
	}

	@Override
	protected ConnectUserDetailDAO getConnectUserDetailDAO() {
		return null;
	}
}
