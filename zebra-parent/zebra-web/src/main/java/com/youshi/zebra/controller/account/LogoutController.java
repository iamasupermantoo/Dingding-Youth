package com.youshi.zebra.controller.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.dorado.push.constants.PushDevice;
import com.dorado.push.service.impl.PushTokenServiceImpl;
import com.dorado.push.utils.PushDeviceUtils;
import com.youshi.zebra.ZebraCommonApiTags;
import com.youshi.zebra.core.web.annotation.LoginRequired;
import com.youshi.zebra.passport.service.UserPassportService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * 退出登陆
 * 
 * @author wangsch
 * @date		2016年11月5日
 *
 */
@RequestMapping(value = "/logout")
@RestController
public class LogoutController {
	private static final Logger logger = LoggerFactory.getLogger(LogoutController.class);
	@Autowired
	private UserPassportService passportService;
	
	@Autowired
	private PushTokenServiceImpl pushTokenService;
	
	@ApiOperation(value = "退出登陆", tags=ZebraCommonApiTags.LOGIN_REGISTER)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query")
	})
	@RequestMapping(value = "", method=RequestMethod.POST)
	@LoginRequired
	public Object logout(
			@ApiIgnore @Visitor Integer userId
			) {
		// 移除ticket secret密钥
		passportService.removeSecretRandom(userId, WebRequestContext.getTicketRandom());
		
//		// 解绑push token
//		PushDevice device = PushDeviceUtils.getDevice(DoradoRequestContext.getAppPlatform());
//		pushTokenService.unbindPushToken(userId, device);
		
		logger.info("User {} logout succ", userId);
		return JsonResultView.SUCCESS;
	}
}
