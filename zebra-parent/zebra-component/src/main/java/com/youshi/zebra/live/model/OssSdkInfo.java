package com.youshi.zebra.live.model;

/**
 * 
 * 处于安全角度，必须使用临时凭据
 * 
 * @author wangsch
 * @date 2017年3月24日
 */
public class OssSdkInfo {
	private String endpoint;
	
	private String accessKeyId;
	
	private String accessKeySecret;
	
	private String bucketName;
	
	private String key;

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}