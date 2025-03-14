package com.youshi.zebra.scholarship;

/**
 * 
 * @author wangsch
 * @date 2017年1月22日
 */
public class WxShareInfo {
	private String appid;
	
	private String secret;
	
	private String url;
	
	private String title;
	
	private String brief;
	
	private String picUrl;
	
	public WxShareInfo(String appid, String secret, String url, String title, String brief, String picUrl) {
		super();
		this.appid = appid;
		this.secret = secret;
		this.url = url;
		this.title = title;
		this.brief = brief;
		this.picUrl = picUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
