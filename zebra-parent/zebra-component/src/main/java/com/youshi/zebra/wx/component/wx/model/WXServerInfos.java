package com.youshi.zebra.wx.component.wx.model;

import com.dorado.framework.crud.model.impl.AbstractModel;

/**
 * 微信公众号，需要的服务信息
 * 
 * @author wangsch
 * @date 2016-09-12
 */
public class WXServerInfos extends AbstractModel<Integer> {
	/**
	 * 公众号appId
	 */
	private String appId;
	
	/**
	 * 公众号自己的access_token
	 */
	private String accessToken;
	
	/**
	 * access_token有效时间，秒
	 */
	private int tokenExpiresIn;
	
	/**
	 * 公众号jsapi ticket
	 */
	private String jsapiTicket;
	
	/**
	 * jsapi ticket有效时间，秒
	 */
	private int ticketExpiresIn;
	
	public WXServerInfos(long createTime, String appId, String accessToken, int tokenExpiresIn, String jsapiTicket,
			int ticketExpiresIn) {
		this.createTime = createTime;
		this.appId = appId;
		this.accessToken = accessToken;
		this.tokenExpiresIn = tokenExpiresIn;
		this.jsapiTicket = jsapiTicket;
		this.ticketExpiresIn = ticketExpiresIn;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public int getTokenExpiresIn() {
		return tokenExpiresIn;
	}

	public void setTokenExpiresIn(int tokenExpiresIn) {
		this.tokenExpiresIn = tokenExpiresIn;
	}

	public String getJsapiTicket() {
		return jsapiTicket;
	}

	public void setJsapiTicket(String jsapiTicket) {
		this.jsapiTicket = jsapiTicket;
	}

	public int getTicketExpiresIn() {
		return ticketExpiresIn;
	}

	public void setTicketExpiresIn(int ticketExpiresIn) {
		this.ticketExpiresIn = ticketExpiresIn;
	}
	
	// TODO
	// unsupported methods
	
}