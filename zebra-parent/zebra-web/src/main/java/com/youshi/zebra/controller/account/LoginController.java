package com.youshi.zebra.controller.account;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.dorado.mvc.utils.CookieUtils;
import com.dorado.mvc.validate.annotation.Length;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.ZebraCommonApiTags;
import com.youshi.zebra.connect.constant.ConnectType;
import com.youshi.zebra.connect.service.ConnectService;
import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.counts.model.UserCountsModel;
import com.youshi.zebra.counts.service.UserCountsService;
import com.youshi.zebra.passport.constant.PassportConstants;
import com.youshi.zebra.passport.service.UserPassportService;
import com.youshi.zebra.register.constant.ConnectBindStatus;
import com.youshi.zebra.register.service.ConnectBindService;
import com.youshi.zebra.user.constant.UserStatus;
import com.youshi.zebra.user.constant.UserType;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.service.UserService;
import com.youshi.zebra.view.UserInfoView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * 登陆相关
 * 
 * Date: May 10, 2016
 * 
 * @author wangsch
 *
 */
@RestController
@RequestMapping("/login")
public class LoginController {

	private Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UserPassportService passportService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@Autowired
	private ConnectBindService connectBindService;

	@ApiOperation(value = "手机密码登录", tags=ZebraCommonApiTags.LOGIN_REGISTER, 
			response=UserInfoView.class)
	@RequestMapping(value = "/mobile", method = RequestMethod.POST)
	public Object mobile(
			@ApiParam(value = "手机号", required=true)
			@RequestParam("mobile") String mobile,
			@ApiParam(value = "密码", required=true)
			@Length(name="密码", min=1)
			@RequestParam("password") String password) {
		Integer userId = passportService.verifyAccount(mobile, password);
		if (userId == null) {
			logger.error("User not found, MOBILE login fail, mobile: {}, password: {}", mobile, password);
			return ZebraMetaCode.LoginFailed.toView();
		}
		
		return doLogin(userId);
	}
	
	@ApiIgnore
	@ApiOperation(value = "第三方登陆，如：qq、weixin", tags=ZebraCommonApiTags.LOGIN_REGISTER, 
			response=UserInfoView.class)
	@RequestMapping(value = "/connect", method=RequestMethod.POST)
	public Object weixin(
			@ApiParam(value = "外部用户id（如：openid）", required=true)
			@RequestParam("externalUserId") String externalUserId,
			@ApiParam(value = "第三方类型。1:qq，2:微信")
			@RequestParam("type") Integer type
			) {
		
		ConnectType connectType = ConnectType.fromValue(type);
		ConnectBindStatus bindStatus = connectBindService.getBindStatus(externalUserId, connectType);
		if(bindStatus != ConnectBindStatus.Binded) {
			return ZebraMetaCode.ConnectNotBinded.toView();
		}
		
		ConnectService connectService = connectBindService.getConnectService(connectType);
		Integer userId = connectService.getUserIdByExternalUserId(externalUserId);
		if(userId == null) {
			logger.error("User not found, connect login fail, externalUserId: {}, type: {}", 
					externalUserId, connectType);
			return ZebraMetaCode.LoginFailed.toView();
		}
		return doLogin(userId);
	}
	
	@Autowired
	private UserCountsService userCountsService;
	
	private JsonResultView doLogin(Integer userId) {
		String ticket = null;
		UserModel user = userService.getById(userId);
		
		UserCountsModel counts = userCountsService.getById(userId);
		user.setCounts(counts);
		
		// 确保这个账号的用户类型是学生
		UserType userType = UserType.fromValue(user.getType());
		if(userType != UserType.Student) {
			logger.error("This account is NOT A STUDENT, MOBILE login fail, userId: {}, userType: {}", 
					userId, userType);
			return ZebraMetaCode.LoginFailed.toView();
		}
		
		if (UserStatus.isNotNormal(user)) {
			logger.error("User blocked, login fail, user_id: {}", userId);
			return ZebraMetaCode.UserBlocked.toView();
		} else {
//			passportService.removeSecret(userId);			// 允许一个账号登录多个设备，不需要移除secret
			ticket = passportService.createTicketWithRand(userId);	// 创建ticket
		}

		JsonResultView result = viewBuilder.buildSingle(user, "user", 
				ExplicitViewMapper.getInstance().setViewClass(UserInfoView.class));
		
		saveCookie(ticket);
		
		logger.info("User login succ. userId: {}, p: {}", userId, ticket);
		return result.addValue(PassportConstants.TICKET_NAME, ticket);
	}
	
	private static final int TICKET_EXPIRE = (int) TimeUnit.DAYS.toSeconds(25);
	
	public void saveCookie(String ticket) {
		HttpServletRequest request = WebRequestContext.getRequest();
		HttpServletResponse response = WebRequestContext.getResponse();
		
		CookieUtils.saveCookie(response, PassportConstants.TICKET_NAME, ticket, TICKET_EXPIRE,
				request.getServerName(), "/", true, false);
	}
	
	
}