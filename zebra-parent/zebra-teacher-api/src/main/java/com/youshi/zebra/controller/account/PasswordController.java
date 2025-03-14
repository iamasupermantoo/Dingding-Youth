package com.youshi.zebra.controller.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.framework.threshold.ThresholdUtils;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.youshi.zebra.ZebraCommonApiTags;
import com.youshi.zebra.core.constants.ZebraThreshold;
import com.youshi.zebra.core.web.annotation.LoginRequired;
import com.youshi.zebra.mobile.service.MobileCodeService;
import com.youshi.zebra.passport.service.UserPassportService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * 密码
 * 
 * @author wangsch
 * @date 2017年1月10日
 */
@RestController
@RequestMapping("/password")
public class PasswordController {
	
	@Autowired
    private MobileCodeService mobileCodeService;

    @Autowired
    private UserPassportService passportService;
    
    @ApiOperation(value = "修改密码", tags={ZebraCommonApiTags.MINE, ZebraCommonApiTags.PASSWD})
    @ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query")
	})
    @RequestMapping(value = "/change", method=RequestMethod.POST)
    @LoginRequired
    public Object change(
    		@ApiIgnore @Visitor Integer userId,
    		@ApiParam(value = "旧密码", required=true)
    		@RequestParam(value = "oldpasswd") String oldpasswd,
    		@ApiParam(value = "新密码", required=true)
    		@RequestParam(value = "newpasswd") String newpasswd
    		) {
    	passportService.changePassword(userId, oldpasswd, newpasswd);
    	
    	return JsonResultView.SUCCESS;
    }
    
    
    /**
     * 发送验证码
     */
    @ApiIgnore
    @RequestMapping(value = "/getCode", method = RequestMethod.POST)
    public JsonResultView getCode(
    		@RequestParam("mobile") String mobile	// 手机号
    		) {
    	boolean allow = allowGetCode(mobile);
		if(!allow) {
			return JsonResultView.OP_TOO_FAST;
		}
        mobileCodeService.generateResetCode(mobile);
        return JsonResultView.SUCCESS;
    
    }
    
    /**
     * 重置密码，忘记密码时
     */
    @ApiIgnore
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public JsonResultView reset(
    		@RequestParam(value = "mobile", required = false) String mobile,		// 手机号
    		@RequestParam("code") String code,										// 验证码
            @RequestParam("password") String password								// 新密码
            ) {
    	passportService.resetPassword(mobile, password, code);
        return JsonResultView.SUCCESS;
    }
    
    private boolean allowGetCode(String mobile) {
		return !ThresholdUtils.isReached(ZebraThreshold.MOBILE_CODE, mobile) 
				&& !ThresholdUtils.isReached(ZebraThreshold.MOBILE_CODE_IP, WebRequestContext.getCurrentIpInString());
	}
}
