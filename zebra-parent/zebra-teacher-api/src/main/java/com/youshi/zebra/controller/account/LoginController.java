package com.youshi.zebra.controller.account;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.validate.annotation.Length;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.ZebraCommonApiTags;
import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.passport.constant.PassportConstants;
import com.youshi.zebra.passport.service.UserPassportService;
import com.youshi.zebra.user.constant.UserStatus;
import com.youshi.zebra.user.constant.UserType;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.service.UserService;
import com.youshi.zebra.view.UserInfoView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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

	@ApiOperation(value = "手机密码登录", tags=ZebraCommonApiTags.LOGIN_REGISTER)
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
	
	private JsonResultView doLogin(Integer userId) {
		String ticket = null;
		UserModel user = userService.getById(userId);
		
		// 确保这个账号的用户类型是老师
		UserType userType = UserType.fromValue(user.getType());
		if(userType != UserType.Teacher) {
			logger.error("This account is NOT A TEACHER, MOBILE login fail, userId: {}, userType: {}", 
					userId, userType);
			return ZebraMetaCode.LoginFailed.toView();
		}
		
		if (UserStatus.isNotNormal(user)) {
			logger.error("User blocked, login fail, user_id: {}", userId);
			return ZebraMetaCode.UserBlocked.toView();
		} else {
			// 删除secret，老师端，不允许一个账号在多个设备登录，
			// 所以每次登录要移除老的secret，让其他设备的登录失效
			// FIXME
//			passportService.removeTicket(userId);
			ticket = passportService.createTicketWithRand(userId);	// 创建ticket
		}

		JsonResultView result = viewBuilder.buildSingle(user, "user", 
				ExplicitViewMapper.getInstance().setViewClass(UserInfoView.class));
		
		logger.info("Teacher login succ. userId: {}, p: {}", userId, ticket);
		return result.addValue(PassportConstants.TICKET_NAME, ticket);
	}
}
