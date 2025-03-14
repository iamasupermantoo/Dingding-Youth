package com.youshi.zebra.connect.service;

import java.util.Collection;
import java.util.Map;

import com.youshi.zebra.connect.constant.ConnectType;
import com.youshi.zebra.connect.exception.RemoteServiceErrorException;
import com.youshi.zebra.connect.model.ConnectUserDetail;

/**
 * 
 * 第三方账号服务
 * 
 * @author wangsch
 * @date 2017年1月10日
 */
public interface ConnectService {
	
	/**
	 * 
	 * 
	 * @return	{@link ConnectType}
	 */
	public ConnectType getConnectType();
    
    //////////////////////////////////////////////系统内->第三方用户，id映射////////////////////////////////////////////
    /**
     * 根据系统内用户id，获取第三方用户id（系统内->第三方）
     * 
     * @param userId	系统内用户id
     * @return			第三方用户id
     */
    public String getExternalUserIdByUserId(int userId);

    /**
     * 根据第三方用户id，获取系统内用户id（第三方->系统内）
     * 
     * @param externalUserId	第三方用户id
     * @return 					系统内用户id
     */
    public Integer getUserIdByExternalUserId(String externalUserId);
    
    /**
     * 根据内部用户id获取第三方用户id，获取映射Map
     * 
     * @param userIds	内部用户id集合
     * @return			Map
     * 					k: 内部用户id
     * 					v: 第三方用户id
     */
    public Map<Integer, String> getExternalUserIdsByUserIds(Collection<Integer> userIds);
    
    //////////////////////////////////////////////绑定相关////////////////////////////////////////////
    /**
     * 外部用户是否绑定到内部账户
     * 
     * @param externalUserId	第三方用户id
     * @return 					已绑定返回true，否则返回false
     */
    public boolean isExternalUserConnect(String externalUserId);

    /**
     * 绑定第三方用户到系统内用户
     * 
     * @param userId			系统内用户id
     * @param accessToken		accessToken
     * @param refreshToken		refreshToken
     * @param externalUserId	第三方用户id
     */
    public void createConnect(int userId, String accessToken, String refreshToken, String externalUserId);

    /**
     * 移除绑定关系
     * 
     * @param userId	系统内用户id
     */
    public void removeConnect(int userId);

    
    ////////////////////////////////////////////// token 相关////////////////////////////////////////////
    /**
     * 获取accessToken，根据第三方用户id。读DB中存的
     * 
     * @param externalUserId	第三方用户id
     * @return					第三方用户accessToken
     */
    public String getAccessToken(String externalUserId);

    /**
     * 获取accessToken，根据系统内用户id。读DB中存的
     * 
     * @param userId	系统内用户id
     * @return			accessToken
     */
    public String getAccessToken(int userId);

    /**
     * 用accessToken获取第三方用户id，调用外部api。
     * 
     * @param accessToken 		accessToken，调用凭据
     * @return					第三方用户id
     * @throws					RemoteServiceErrorException	远程服务调取失败
     */
    public String getExternalUserIdByAccessToken(String accessToken)
            throws RemoteServiceErrorException;

    /**
     * 用accessToken获取第三方用户id，调用外部api，和{@link #getExternalUserIdByAccessToken(String)}不同在于，带重试次数
     * 
     * @param accessToken 		accessToken，调用凭据
     * @param retryTimes 		重试次数
     * @return					第三方用户id
     * @throws					RemoteServiceErrorException	远程服务调取失败
     * 
     * @see	#getExternalUserIdByAccessToken(String)
     * 
     */
    public String getExternalUserIdByAccessToken(String accessToken, int retryTimes)
            throws RemoteServiceErrorException;
    
    //////////////////////////////////////////////外部用户信息////////////////////////////////////////////
    public void setConnectUserDetail(String externalUserId, String name, String userJson);
    
    public ConnectUserDetail getConnectUserDetail(String externalUserId);
    

}
