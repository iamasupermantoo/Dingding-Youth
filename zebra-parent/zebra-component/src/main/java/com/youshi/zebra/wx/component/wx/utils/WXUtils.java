package com.youshi.zebra.wx.component.wx.utils;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dorado.framework.event.utils.PerfUtils;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.dorado.mvc.utils.CookieUtils;
import com.ecyrd.speed4j.StopWatch;
import com.github.phantomthief.util.ObjectMapperUtils;
import com.youshi.zebra.core.utils.HttpClientHolder;
import com.youshi.zebra.wx.component.wx.constants.WXFWHConstants;

import weixin.popular.api.MediaAPI;
import weixin.popular.api.MenuAPI;
import weixin.popular.api.SnsAPI;
import weixin.popular.api.TicketAPI;
import weixin.popular.bean.media.MediaGetResult;
import weixin.popular.bean.menu.MenuButtons;
import weixin.popular.bean.sns.SnsToken;
import weixin.popular.bean.ticket.Ticket;
import weixin.popular.bean.token.Token;
import weixin.popular.bean.user.User;

/**
 * 
 * 微信公众号工具类
 * 
 * @author wangsch
 * @date 2017年4月21日
 */
public class WXUtils {
	public static final String EXTERNAL_USER_ID = "__e_u_i__";
	
	public static final String NICK_NAME = "__n_n__";
	
	public static final String HEAD_IMAGE_URL = "__h_i_u__";
	
	public static final int COOKIE_EXPIRE = (int)TimeUnit.HOURS.toSeconds(3);
	
	/**
	 * 获取当前微信号的openid(或unionid）
	 * 
	 * @return 
	 */
	public static final String getExternalUserId() {
		HttpServletRequest request = WebRequestContext.getRequest();
		String externalUserId = CookieUtils.getCookie(request, EXTERNAL_USER_ID);
		if(StringUtils.isEmpty(externalUserId)) {
			return null;
		}
		return new String(Base64.decodeBase64(externalUserId.getBytes()));
	}
	
	public static final String getExternalUserId(boolean base64) {
		HttpServletRequest request = WebRequestContext.getRequest();
		String externalUserId = CookieUtils.getCookie(request, EXTERNAL_USER_ID);
		if(StringUtils.isEmpty(externalUserId)) {
			return null;
		}
		return base64 ? new String(Base64.decodeBase64(externalUserId.getBytes())) : externalUserId;
	}
	
	public static final String getHeadImgUrl() {
		HttpServletRequest request = WebRequestContext.getRequest();
		String headImageUrl = CookieUtils.getCookie(request, HEAD_IMAGE_URL);
		if(StringUtils.isEmpty(headImageUrl)) {
			return null;
		}
		return new String(Base64.decodeBase64(headImageUrl.getBytes()));
	}
	
	public static final String getHeadImgUrl(boolean base64) {
		HttpServletRequest request = WebRequestContext.getRequest();
		String headImageUrl = CookieUtils.getCookie(request, HEAD_IMAGE_URL);
		if(StringUtils.isEmpty(headImageUrl)) {
			return null;
		}
		return base64 ? new String(Base64.decodeBase64(headImageUrl.getBytes())) : headImageUrl;
	}

	private static final Logger logger = LoggerFactory.getLogger(WXUtils.class);
	
	/**
	 * 第一步：公众号获取网页授权url
	 * 
	 * @param redirectUrl	回调地址		
	 * @param state			微信回调时的回传参数
	 * @return				网页授权url
	 */
	public static final String getAuthUrl(String redirectUrl, String state, boolean snsapiUserinfo) {
		return SnsAPI.connectOauth2Authorize(WXFWHConstants.APPID, 
				redirectUrl, snsapiUserinfo, state);
	}
	
	/**
	 * 
	 * 第二步：获取用户微信token
	 * 
	 * @param code					code参数
	 * @return 						{@link SnsToken}
	 * 
	 */
	public static SnsToken getAccessToken(String code) {
		StopWatch watcher = PerfUtils.getWatcher("WXUtils.getAccessToken");
		SnsToken token = SnsAPI.oauth2AccessToken(WXFWHConstants.APPID,
				WXFWHConstants.SECRET, code);
		watcher.stop();
		return token;
	}

	/**
	 * 第三步：获取用户信息
	 * 
	 * @param openId					用户微信openId
	 * @param access_token		用户微信token
	 * @return								{@link User}
	 */
	public static User getUserInfo(String openId, String access_token) {
		return SnsAPI.userinfo( access_token, openId, "zh_CN");
	}

	/**
	 * 下载媒体文件
	 * 
	 * @param accessToken		公众号token
	 * @param mediaId			媒体id
	 * @return							{@link MediaGetResult}
	 */
	public static MediaGetResult getMedia(String accessToken, String mediaId) {
		StopWatch watcher = PerfUtils.getWatcher("WXUtils.getMedia");
		MediaGetResult media = MediaAPI.mediaGet(accessToken, mediaId);
		watcher.stop();
		return media;
	}

	// ----------------------------------------------------for jssdk-------------------------------------------------------
	/**
	 * jssdk api列表
	 * 
	 */
	private static final List<String> JS_API_LIST = Arrays.asList("chooseWXPay");
	
	/**
	 * 获取公众号token（公众号自己的token，区别于用户网页授权得到的“用户微信token”）
	 * 
	 * @return {@link Token}
	 */
	public static Token getServiceToken() {
		StopWatch watcher = PerfUtils.getWatcher("WXUtils.getServiceToken");
		
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential"
				+ "&appid=" + WXFWHConstants.APPID
				+ "&secret=" + WXFWHConstants.SECRET;
		
		HttpResponse response = null;
		String content = null;
		Token token = new Token();
		try {
			HttpGet request = new HttpGet(url);
			response = HttpClientHolder.getHttpClient().execute(request);
			content = EntityUtils.toString(response.getEntity(), 
					Charset.forName("UTF-8").displayName());
			
			Map<String, Object> dataMap = ObjectMapperUtils.fromJSON(content, 
					HashMap.class, String.class, Object.class);
			
			String accessToken = (String)dataMap.get("access_token");
			Integer expiresIn = (Integer)dataMap.get("expires_in");
			
			token.setAccess_token(accessToken);
			token.setExpires_in(expiresIn);
			watcher.stop();
		} catch (Exception e) {
			logger.error("Fail get service token, status line: {}, response json: {}", 
					response.getStatusLine(),  content);
		}
		return token;
	}
	
	/**
	 * 获取jssdk ticiet
	 * 
	 * @param serviceToken 公众号token
	 * @return {@link Ticket}
	 */
	public static Ticket getJsTicket(String serviceToken) {
		StopWatch watcher = PerfUtils.getWatcher("WXUtils.getTicket");
		Ticket ticket = TicketAPI.ticketGetticket(serviceToken);
		watcher.stop();
		return ticket;
	}
	
	/**
	 * 获取jssdk配置参数
	 * 
	 * @param url					前端页面url
	 * @param jsapiTicket			公众号jssdk ticket
	 * @return						{@link WXJsConfig}
	 */
	public static WXJsConfig getJsConfig(String url, String jsapiTicket) {
		/*
		 * jsapi_ticket=sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg
		 * &noncestr=Wm3WZYTPz0wzccnW
		 * &timestamp=1414587457
		 * &url=http://mp.weixin.qq.com?params=value
		 */
		String noncestr = RandomStringUtils.randomAlphabetic(6);
		long timestamp = System.currentTimeMillis()/1000;
		StringBuilder sb = new StringBuilder()
			.append("jsapi_ticket=").append(jsapiTicket)
			.append("&noncestr=").append(noncestr)
			.append("&timestamp=").append(timestamp)
			.append("&url=").append(url);
		String signature = DigestUtils.sha1Hex(sb.toString());
		
		return new WXJsConfig(WXFWHConstants.APPID, timestamp, noncestr, 
				signature, JS_API_LIST);
	}
	
	/**
	 * 配置jssdk需要的参数
	 * 
	 * @author wangsch
	 * @date 2016-09-12
	 */
	public static class WXJsConfig {
		private String appId;
		private long timestamp;
		private String nonceStr;
		private String signature;
		private List<String> jsApiList;
		
		public WXJsConfig(String appId, long timestamp, 
				String nonceStr, String signature, List<String> jsApiList) {
			this.appId = appId;
			this.timestamp = timestamp;
			this.nonceStr = nonceStr;
			this.signature = signature;
			this.jsApiList = jsApiList;
		}
		
		public String getAppId() {
			return appId;
		}
		public void setAppId(String appId) {
			this.appId = appId;
		}
		public long getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}
		public String getNonceStr() {
			return nonceStr;
		}
		public void setNonceStr(String nonceStr) {
			this.nonceStr = nonceStr;
		}
		public String getSignature() {
			return signature;
		}
		public void setSignature(String signature) {
			this.signature = signature;
		}
		public List<String> getJsApiList() {
			return jsApiList;
		}
		public void setJsApiList(List<String> jsApiList) {
			this.jsApiList = jsApiList;
		}
	}
	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^end jssdk^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	
	
	/**
	 * 初始化公众号菜单
	 * 
	 * @param serviceToken	公众号token
	 * @param menuButtons	{@link MenuButtons}
	 */
	public static void initMenu(String serviceToken, MenuButtons menuButtons) {
		StopWatch watcher = PerfUtils.getWatcher("WXUtils.initMenu");
		MenuAPI.menuCreate(serviceToken, menuButtons);
		watcher.stop();
	}
}
