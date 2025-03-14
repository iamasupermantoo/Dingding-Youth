package com.youshi.zebra.controller.account;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.ZebraCommonApiTags;
import com.youshi.zebra.connect.constant.ConnectType;
import com.youshi.zebra.connect.model.ConnectBindInfo;
import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.core.web.annotation.LoginRequired;
import com.youshi.zebra.mobile.service.MobileCodeService;
import com.youshi.zebra.passport.constant.PassportConstants;
import com.youshi.zebra.passport.service.UserPassportService;
import com.youshi.zebra.register.constant.ConnectBindStatus;
import com.youshi.zebra.register.service.ConnectBindService;
import com.youshi.zebra.user.constant.UserStatus;
import com.youshi.zebra.user.constant.UserType;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.service.UserService;
import com.youshi.zebra.view.UserInfoView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 第三方账号绑定相关
 * 
 * @author wangsch
 * @date 2017年1月10日
 */
@ApiIgnore
@RestController
@RequestMapping("/connect")
public class ConnectController {
	private Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UserPassportService passportService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@Autowired
	private ConnectBindService connectBindService;
	
	@Autowired
	private MobileCodeService mobileCodeService;
	
	@ApiOperation(value = "各个第三方账号（如：微信、qq等）的绑定状态查询", 
			tags={ZebraCommonApiTags.MINE, ZebraCommonApiTags.CONNECT}, response=ConnectBindInfo.class)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query")
	})
	@RequestMapping(value = "/bind/list", method=RequestMethod.GET)
	@LoginRequired
	public Object bindList(
			@ApiIgnore @Visitor Integer userId
			) {
		JsonResultView result = new JsonResultView();
		List<ConnectBindInfo> list = connectBindService.getBindList(userId, Arrays.asList(ConnectType.values()));
		for (ConnectBindInfo bind : list) {
			result.addValue(bind.getType(), bind);
		}
		return result;
	}
	
	@ApiOperation(value = "绑定。个人设置页面，绑定第三方账号", tags=ZebraCommonApiTags.CONNECT)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query")
	})
	@RequestMapping(value = "/bind", method=RequestMethod.POST)
	@LoginRequired
	public Object bind(
			@ApiIgnore @Visitor Integer userId,
			@ApiParam(value = "第三方账号类型，1:qq，2:微信")
			@RequestParam(value = "type") Integer type,
			@ApiParam(value = "第三方账号id", required=true)
			@RequestParam(value = "externalUserId") String externalUserId,
			@ApiParam(value = "第三方账号access token", required=true)
			@RequestParam(value = "accessToken") String accessToken,
			@ApiParam(value = "第三方账号refresh token")
			@RequestParam(value = "refreshToken", required=false) String refreshToken,
			@ApiParam(value = "第三方账号名（since 2017-02-09）", required=true)
			@RequestParam(value = "name") String name
			) {
		ConnectType connType = ConnectType.fromValue(type);
		connectBindService.bindConnect(userId, connType, accessToken, refreshToken, externalUserId);
		connectBindService.getConnectService(connType).setConnectUserDetail(externalUserId, name, null);
		return JsonResultView.SUCCESS;
	}
	
	/**
	 * 解绑
	 */
	@ApiOperation(value = "解除绑定", tags=ZebraCommonApiTags.CONNECT)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query")
	})
	@RequestMapping(value = "/unbind", method=RequestMethod.POST)
	@LoginRequired
	public Object unbind(
			@ApiIgnore @Visitor Integer userId,
			@RequestParam(value = "type") Integer type
			) {
		connectBindService.unbindConnect(userId, ConnectType.fromValue(type));
		
		return JsonResultView.SUCCESS;
	}
	
	/**
	 * 获取验证码。第三方登录时，绑定第三方账号
	 */
	@ApiIgnore
	@RequestMapping(value = "/getCode", method = RequestMethod.POST)
	public Object getCode(
			@RequestParam(value = "mobile") String mobile,
			@RequestParam(value = "type") Integer type
			) {
		
		ConnectBindStatus status = connectBindService.getBindStatus(mobile, ConnectType.Mobile);
		// 注册验证码
		if(status != ConnectBindStatus.Binded) {
			mobileCodeService.generateRegisterCode(mobile);
			return JsonResultView.SUCCESS;
		}
		
		// 判断是否已经和其他第三方账号绑定
		Integer userId = connectBindService.getConnectService(ConnectType.Mobile)
				.getUserIdByExternalUserId(mobile);
		ConnectType connType = ConnectType.fromValue(type);
		ConnectBindStatus bindStatus = connectBindService.getBindStatus(userId, connType);
		if(bindStatus == ConnectBindStatus.Binded) {
			return ZebraMetaCode.ConnectAlreadyBinded;
		}
		
		// 绑定验证码
		mobileCodeService.generateConnectCode(mobile);
		return ZebraMetaCode.MobileAlreadyBinded.toView();
	}
	
	@ApiOperation(value = "绑定。第三方登录时，绑定第三方账号", tags=ZebraCommonApiTags.CONNECT)
	@RequestMapping(value = "/login/bind", method = RequestMethod.POST)
	public Object loginBind(
			@ApiParam(value = "手机号", required=true)
			@RequestParam(value = "mobile") String mobile,
			@ApiParam(value = "验证码", required=true)
			@RequestParam(value = "code") String code,
			@ApiParam(value = "第三方账号类型，1:qq，2:微信")
			@RequestParam(value = "type") Integer type,
			@ApiParam(value = "第三方账号id", required=true)
			@RequestParam(value = "externalUserId") String externalUserId,
			@ApiParam(value = "第三方账号access token", required=true)
			@RequestParam(value = "accessToken") String accessToken,
			@ApiParam(value = "第三方账号refresh token")
			@RequestParam(value = "refreshToken", required=false) String refreshToken,
			@ApiParam(value = "第三方账号名（since 2017-02-09）", required=true)
			@RequestParam(value = "nickname") String nickname
			) {
		
		mobileCodeService.verifyConnectCode(mobile, code);
		Integer userId = connectBindService.getConnectService(ConnectType.Mobile)
				.getUserIdByExternalUserId(mobile);
		
		ConnectType connType = ConnectType.fromValue(type);
		connectBindService.bindConnect(userId, connType, accessToken, refreshToken, externalUserId);
		connectBindService.getConnectService(connType).setConnectUserDetail(externalUserId, nickname, null);
		
		JsonResultView result = doLogin(userId);
		return result;
	}
	
	
	private JsonResultView doLogin(Integer userId) {
		String ticket = null;
		UserModel user = userService.getById(userId);
		
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
		
		logger.info("User login succ. userId: {}, p: {}", userId, ticket);
		return result.addValue(PassportConstants.TICKET_NAME, ticket);
	}
	
	
}
