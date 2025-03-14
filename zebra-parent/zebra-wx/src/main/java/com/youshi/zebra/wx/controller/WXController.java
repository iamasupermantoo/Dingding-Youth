package com.youshi.zebra.wx.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.dorado.mvc.utils.CookieUtils;
import com.youshi.zebra.wx.component.wx.constants.WXFWHConstants;
import com.youshi.zebra.wx.component.wx.service.WXService;
import com.youshi.zebra.wx.component.wx.utils.WXUtils;
import com.youshi.zebra.wx.component.wx.utils.WXUtils.WXJsConfig;

import springfox.documentation.annotations.ApiIgnore;
import weixin.popular.bean.sns.SnsToken;

/**
 * 微信公众号相关的Controller
 * 
 * @author wangsch
 * @date 2017年4月20日
 */
@Controller
public class WXController {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WXController.class);
	
	@Autowired
	private WXService wxService;
	
	/**
	 * 菜单：订单
	 */
	@RequestMapping(value = "/menu/order", method=RequestMethod.GET)
	public ModelAndView order(
			HttpServletResponse response
			) {
		// 是否已经微信授权
		String externalUserId = WXUtils.getExternalUserId();
		if(StringUtils.isEmpty(externalUserId)) {
			String authUrl = WXUtils.getAuthUrl(WXFWHConstants.WX_AUTH_REDIRECT_URI, null, false);
			return new ModelAndView("redirect:" + authUrl);
		}
		
		// 是否已经登录
		Integer userId = WebRequestContext.getUserId();
		if(userId == null) {
			return new ModelAndView("redirect:/login.html");
		} else {
			return new ModelAndView("redirect:/orders.html");
		}
	}
	
	
	/**
	 * 微信获取用户信息，授权回调
	 */
	@RequestMapping(value = "/auth/callback", method = RequestMethod.GET)
	public ModelAndView callback(
			@RequestParam(value = "code") String code, 
			@RequestParam(value = "state") String state,
			HttpServletRequest request, HttpServletResponse response) {
		if(StringUtils.isEmpty(code)) {
			logger.error("Wx auth callback fail, cause CODE IS EMPTY.");
			return new ModelAndView("forward:/common/500?msg=code为空");
		}
		SnsToken token = WXUtils.getAccessToken(code);
		if(!token.isSuccess()) {
			logger.error("Wx auth callback fail, cause TOKEN NOT SUCCESS.");
			return new ModelAndView("forward:/common/500?msg=获取token失败");
		}
		
		String openId = token.getOpenid();
		
		if(StringUtils.isEmpty(openId)) {
			logger.error("Wx auth callback fail, cause openId empty.");
			return new ModelAndView("forward:/common/500?msg=获取用户信息失败");
		}
		
		// 保存id和昵称到cookie中
		String encodeOpenId = Base64.encodeBase64URLSafeString(openId.getBytes());
		
		logger.info("Wx callback ok. openId: {}, encodeOpenId: {}, state: {}", 
				openId, encodeOpenId, state);
		
		CookieUtils.saveCookie(response, WXUtils.EXTERNAL_USER_ID, encodeOpenId,
				WXUtils.COOKIE_EXPIRE, request.getServerName(), "/", false);
		
		// 转发
		return new ModelAndView("redirect:/login.html");
	}
	
	/**
	 * jssdk配置参数
	 */
	@RequestMapping(value = "/jssdk/config", method=RequestMethod.GET)
	@ResponseBody
	public Object wxConfig(@RequestParam(value = "url") String url) {
		WXJsConfig wxConfig = wxService.getWXJsConfig(url);
		return new JsonResultView().addValue("config", wxConfig);
	}
}
