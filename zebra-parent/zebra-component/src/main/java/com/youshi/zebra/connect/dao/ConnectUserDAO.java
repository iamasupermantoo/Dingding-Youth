package com.youshi.zebra.connect.dao;

import java.util.Collection;
import java.util.Map;

import com.youshi.zebra.connect.model.ConnectUser;

/**
 * 操作系统内用户id和第三方用户id的映射关系，是一对一的，映射关系用{@link ConnectUser}代表。
 * 
 * @author wangsch
 * @date 2017年1月10日
 */
public interface ConnectUserDAO {
	
	/**
	 * 根据第三方用户id获取
	 * 
	 * @param externalUserId	外部用户id
	 * @return					{@link ConnectUser}，或者null
	 */
    public ConnectUser getByExternalUserId(String externalUserId);
    
    /**
     * 根据系统内用户id获取
     * 
     * @param userId	系统内用户id
     * @return			{@link ConnectUser}，或者null
     */
    public ConnectUser getByUserId(int userId);
    
    /**
     * 根据第三方用户id集合，获取映射关系Map
     * 
     * @param externalUserIds	第三方用户id集合
     * @return					Map，或者空Map{@link Collections#emptyMap()}
     * 							k: 第三方用户id
     * 							v: 系统内用户id
     */
    public Map<String, Integer> getByExternalUserIds(Collection<String> externalUserIds);
    
    /**
     * 
     * 
     * @param userIds
     * @return
     */
    public Map<Integer, String> getByByUserIds(Collection<Integer> userIds);

    /**
     * 
     * @param userId
     * @param externalUserId
     * @param createTime
     * @return
     */
    public int insert(int userId, String externalUserId, long createTime);
    
    /**
     * 
     * 
     * @param userId	系统内用户id
     * @return 影响行数
     */
    public int remove(int userId);
}
