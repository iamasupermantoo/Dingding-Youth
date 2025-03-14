package com.youshi.zebra.connect.dao;

import com.youshi.zebra.connect.model.AccessToken;

/**
 * {@link AccessToken} DAO接口定义
 * 
 * @author wangsch
 * @date 2017年1月10日
 */
public interface AccessTokenDAO {
	
	/**
	 * 根据access token获取
	 * 
	 * @param accessToken access token字符串
	 * @return {@link AccessToken}
	 */
    public AccessToken getAccessToken(String accessToken);
    
    /**
     * 根据外部用户id获取
     * 
     * @param externalUserId 外部用户id获取
     * @return {@link AccessToken}
     */
    public AccessToken getAccessTokenByExternalUser(String externalUserId);

    /**
     * 写入／更新一条记录
     * 
     * @param externalUserId
     * @param accessToken
     * @param refreshToken
     * @param time
     * @return
     */
    public int insertOrUpdateAccessToken(String externalUserId, String accessToken, String refreshToken, long time);
}
