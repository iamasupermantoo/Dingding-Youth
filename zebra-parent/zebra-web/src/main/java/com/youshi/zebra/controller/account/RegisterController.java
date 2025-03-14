package com.youshi.zebra.controller.account;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.framework.threshold.ThresholdUtils;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.ZebraCommonApiTags;
import com.youshi.zebra.connect.constant.ConnectType;
import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.core.constants.ZebraThreshold;
import com.youshi.zebra.mobile.service.MobileCodeService;
import com.youshi.zebra.passport.constant.PassportConstants;
import com.youshi.zebra.passport.service.UserPassportService;
import com.youshi.zebra.register.service.ConnectBindService;
import com.youshi.zebra.register.service.RegisterService;
import com.youshi.zebra.stats.service.UserStatsService;
import com.youshi.zebra.stats.utils.StatsUtils;
import com.youshi.zebra.user.constant.UserType;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.service.UserService;
import com.youshi.zebra.view.UserInfoView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author wangsch
 * @date 2016年12月29日
 */
@RestController
@RequestMapping("/register")
public class RegisterController {
	@Autowired
	private MobileCodeService mobileCodeService;

	@Autowired
	private RegisterService registerService;

	@Autowired
	private UserPassportService passportService;
	
	@Autowired
	private ConnectBindService connectBindService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserStatsService userStatsService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@ApiOperation(value = "手机注册，获取验证码", tags=ZebraCommonApiTags.LOGIN_REGISTER)
	@RequestMapping(value = "/getCode", method=RequestMethod.POST)
    public Object getCode(
    		@ApiParam(value = "手机号", required=true)
    		@RequestParam("mobile") String mobile
    		) {
		boolean allow = allowGetCode(mobile);
		if(!allow) {
			return JsonResultView.OP_TOO_FAST;
		}
		mobileCodeService.generateRegisterCode(mobile);
		return JsonResultView.SUCCESS;
    }
	
	@ApiOperation(value = "手机注册，验证验证码", tags=ZebraCommonApiTags.LOGIN_REGISTER)
	@RequestMapping(value = "/verifyCode", method=RequestMethod.POST)
	public Object verify(
			@ApiParam(value = "手机号", required=true)
			@RequestParam("mobile") String mobile,
			@ApiParam(value = "验证码", required=true)
			@RequestParam("code") String verifyCode
			) {
		mobileCodeService.verifyRegisterCode(mobile, verifyCode);
		return JsonResultView.SUCCESS;
	}
	
	@ApiOperation(value = "手机注册，完成", tags=ZebraCommonApiTags.LOGIN_REGISTER, 
			response=UserInfoView.class)
	@RequestMapping(value = "/mobile", method=RequestMethod.POST)
	public Object register(
			@ApiParam(value = "手机号", required=true)
			@RequestParam("mobile") String mobile, 							// 手机号
			@ApiParam(value = "密码", required=true)
			@RequestParam("password") String password,						// 密码
			@ApiParam(value = "确认密码", required=true)
			@RequestParam("confirm") String confirmPassword,				// 确认密码
			
			@ApiParam(value = "出生日期。如：2017-11-11", required=true)
			@RequestParam(value = "birthday") String birthday,
			
//			@ApiParam(value = "昵称", required=true)
//			@RequestParam("name") String name,								// 昵称
//			@ApiParam(value = "个性签名")
//			@RequestParam(value = "signature", required = false) String signature,					// 签名
//			@ApiParam(value = "头像")
//			@RequestParam(value = "headImage", required = false) MultipartFile image,				// 头像
			HttpServletRequest request
			) {
		String name = "dd" + RandomUtils.nextInt(1000, 9000);
		Integer userId = registerService.registerMobile(name, UserType.Student, 
				mobile, password, StringUtils.EMPTY, null, birthday);
		
		if (userId != null && userId != 0) {
			connectBindService.bindConnect(userId, ConnectType.Mobile, null, null, mobile);
			
			// 注册用户数
			userStatsService.dealAcqNormalUser(StatsUtils.today(), userId);
			
			return doLogin(userId);
		}

		return ZebraMetaCode.ServerFailed.toView();
	}
	
	// -----------------------------------------private methods-----------------------------------------
	/**
	 * 
	 * 注册成功后，自动执行登陆。
	 * 
	 * @param userId		用户id
	 * @return		{@link JsonResultView}
	 */
	private Object doLogin(Integer userId) {
		// 登录放票
		String ticket = passportService.createTicketWithRand(userId);
		UserModel user = userService.getById(userId);
		
		JsonResultView result = viewBuilder.buildSingle(user, "user", 
				ExplicitViewMapper.getInstance().setViewClass(UserInfoView.class));
		return result.addValue(PassportConstants.TICKET_NAME, ticket);
	}
	
	/**
	 * 是否允许获取验证码，阀值检测
	 * 
	 * @param mobile		手机号
	 * @return						true允许，false不允许
	 */
	private boolean allowGetCode(String mobile) {
		return !ThresholdUtils.isReached(ZebraThreshold.MOBILE_CODE, mobile) 
				&& !ThresholdUtils.isReached(ZebraThreshold.MOBILE_CODE_IP, WebRequestContext.getCurrentIpInString());
	}
	
}
