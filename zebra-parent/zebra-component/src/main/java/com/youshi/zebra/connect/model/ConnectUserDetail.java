package com.youshi.zebra.connect.model;

import java.util.HashMap;
import java.util.Map;

import com.github.phantomthief.util.ObjectMapperUtils;

/**
 * 外部用户详细信息
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
public class ConnectUserDetail {
	public enum ConnectUserDetailKeys{
		name,
		user_json
	}
	
	private String externalUserId;
	
	private String data;
	
	private long createTime;

	private transient Map<String, Object> resolvedData;
	
	public ConnectUserDetail(String externalUserId, String data, long createTime) {
		this.externalUserId = externalUserId;
		this.data = data;
		this.createTime = createTime;
	}

	public String getExternalUserId() {
		return externalUserId;
	}

	public void setExternalUserId(String externalUserId) {
		this.externalUserId = externalUserId;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	public Map<String, Object> resolvedData() {
        if (resolvedData == null) {
            synchronized (this) {
                if (resolvedData == null) {
                    resolvedData = ObjectMapperUtils.fromJSON(data, HashMap.class, String.class,
                            Object.class);
                }
            }
        }
        
        return resolvedData;
    }
	
//
//    public boolean isVerified();
//
//    public String getHeadImageUrl();
//
    public String getName() {
    	return (String)resolvedData().get(ConnectUserDetailKeys.name.name());
    }
//
//
//    public Map<String, Object> resolvedData();
}
