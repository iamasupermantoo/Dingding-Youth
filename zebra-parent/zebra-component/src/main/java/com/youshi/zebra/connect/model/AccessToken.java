package com.youshi.zebra.connect.model;

import java.util.Date;

import com.dorado.framework.crud.model.HasId;

/**
 * 外部用户的access token信息，授权成功后，能够获取如：用户id，access token，refresh token信息。
 * 
 * @author wangsch
 * @date 2017年1月10日
 */
public class AccessToken implements HasId<Integer> {

    private final int id;

    private final String accessToken;
    
    private final String refreshToken;

    private final String externalUserId;

    private final long createTime;

    public AccessToken(int id, String accessToken, String refreshToken, String externalUserId, long createTime) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.externalUserId = externalUserId;
        this.createTime = createTime;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }
    
	public String getRefreshToken() {
		return refreshToken;
	}

    public String getExternalUserId() {
        return externalUserId;
    }

    public Date getCreateTime() {
        return new Date(createTime);
    }
}
