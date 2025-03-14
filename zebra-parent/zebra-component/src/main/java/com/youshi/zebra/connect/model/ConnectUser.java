package com.youshi.zebra.connect.model;

import java.util.Date;

/**
 * 系统内用户id和第三方用户id映射关系，一对一关系
 * 
 * @author wangsch
 * @date 2017年1月10日
 */
public class ConnectUser {
	
	/**
	 * 系统内用户id
	 */
    private final int userId;

    /**
     * 第三方用户id
     */
    private final String externalUserId;
    
    /**
     * 创建时间
     */
    private final long createTime;

    public ConnectUser(int userId, String externalUserId, long createTime) {
        this.userId = userId;
        this.externalUserId = externalUserId;
        this.createTime = createTime;
    }

    public int getUserId() {
        return userId;
    }

    public String getExternalUserId() {
        return externalUserId;
    }

    public Date getCreateTime() {
        return new Date(createTime);
    }
}
