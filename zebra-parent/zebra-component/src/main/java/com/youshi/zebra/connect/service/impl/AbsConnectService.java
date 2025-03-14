package com.youshi.zebra.connect.service.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.dorado.framework.utils.DoradoMapperUtils;
import com.youshi.zebra.connect.dao.AccessTokenDAO;
import com.youshi.zebra.connect.dao.ConnectUserDAO;
import com.youshi.zebra.connect.dao.ConnectUserDetailDAO;
import com.youshi.zebra.connect.exception.RemoteServiceErrorException;
import com.youshi.zebra.connect.model.AccessToken;
import com.youshi.zebra.connect.model.ConnectUser;
import com.youshi.zebra.connect.model.ConnectUserDetail;
import com.youshi.zebra.connect.service.ConnectService;
import com.youshi.zebra.core.utils.DAOUtils;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年1月10日
 */
public abstract class AbsConnectService implements ConnectService {

	protected org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(getClass());

	protected abstract AccessTokenDAO getAccessTokenDAO();

	protected abstract ConnectUserDAO getConnectUserDAO();
	
	protected abstract ConnectUserDetailDAO getConnectUserDetailDAO();

	@Override
	public String getExternalUserIdByUserId(int userId) {
		return getExternalUserIdsByUserIds(Collections.singleton(userId)).get(userId);
	}

	@Override
	public String getExternalUserIdByAccessToken(String accessToken)
			throws RemoteServiceErrorException {
		return getExternalUserIdByAccessToken(accessToken, 1);
	}
	
	@Override
	public Map<Integer, String> getExternalUserIdsByUserIds(
			Collection<Integer> userIds) {
		return getConnectUserDAO().getByByUserIds(userIds);
	}

	@Override
	public Integer getUserIdByExternalUserId(String externalUserId) {
		ConnectUser connectUser = getConnectUserDAO().getByExternalUserId(
				externalUserId);
		if (connectUser != null) {
			return connectUser.getUserId();
		}
		return null;
	}

	@Override
	public boolean isExternalUserConnect(String externalUserId) {
		return getConnectUserDAO().getByExternalUserId(externalUserId) != null;
	}

	@Override
	public String getAccessToken(String externalUserId) {
		AccessToken accessToken = getAccessTokenDAO().getAccessTokenByExternalUser(externalUserId);
		if (accessToken != null) {
			return accessToken.getAccessToken();
		}
		return null;
	}

	@Override
	public String getAccessToken(int userId) {
		String externalUserId = getExternalUserIdByUserId(userId);
		if (externalUserId != null) {
			return getAccessToken(externalUserId);
		}
		return null;
	}

	protected String resolveRealAccessToken(String rawAccessToken) {
		return rawAccessToken;
	}

	@Override
	public void createConnect(int userId, String accessToken, String refreshToken,
			String externalUserId) {
		long createTime = System.currentTimeMillis();
		getConnectUserDAO().insert(userId, externalUserId, createTime);
		AccessTokenDAO accessTokenDAO = getAccessTokenDAO();
		if(accessTokenDAO != null) {
			accessTokenDAO.insertOrUpdateAccessToken(externalUserId, accessToken, refreshToken, createTime);
		}
	}

	@Override
	public void removeConnect(int userId) {
		ConnectUserDAO connectUserDAO = getConnectUserDAO();
		if(connectUserDAO != null) {
			connectUserDAO.remove(userId);
		}
	}
	
	@Override
	public void setConnectUserDetail(String externalUserId, String name, String userJson) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("name", name);
		dataMap.put("user_json", userJson);
		
		String data = DoradoMapperUtils.toJSON(dataMap);
		int c = getConnectUserDetailDAO().insertOrUpdateConnectUserDetail(externalUserId, data, System.currentTimeMillis());
		DAOUtils.checkInsertDuplicate(c);
	}
	
	@Override
	public ConnectUserDetail getConnectUserDetail(String externalUserId) {
		return getConnectUserDetailDAO().getConnectUserDetail(externalUserId);
	}
	
//	public abstract <T extends ConnectUserDetail> T parseConnectUserDetail(ConnectUserDetail detail);
	
	
}
