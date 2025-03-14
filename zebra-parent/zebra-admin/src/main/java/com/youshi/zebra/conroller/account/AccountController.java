package com.youshi.zebra.conroller.account;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.admin.adminuser.annotation.PrivilegeRequired;
import com.youshi.zebra.admin.adminuser.model.AdminUserPassport;
import com.youshi.zebra.admin.adminuser.service.AdminUserPassportService;
import com.youshi.zebra.admin.adminuser.service.AdminUserService;
import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.service.UserService;
import com.youshi.zebra.view.AdminUserInfoView;

/**
 * 平台端，账号信息
 * 
 * @author wangsch
 * @date 2017年5月23日
 */
@RequestMapping(value = "/account")
@Controller
public class AccountController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AdminUserPassportService adminUserPassportService;
	
	@Autowired
    private AdminUserService adminUserService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	/**
	 * 账号信息
	 */
	@RequestMapping(value = "/info", method=RequestMethod.GET)
	@PrivilegeRequired
	public ModelAndView info(
			@Visitor Integer userId
			) {
		ModelAndView mav = new ModelAndView("account/info_view");
		UserModel user = userService.getById(userId);
		Map<String, Object> dataMap = viewBuilder.buildSingleToMap(user, "details", 
				ExplicitViewMapper.getInstance().setViewClass(AdminUserInfoView.class));
		
		mav.addAllObjects(dataMap);
		return mav;
	}
	
	@RequestMapping(value = "/update", method=RequestMethod.GET)
	public ModelAndView updateUI(
			@Visitor Integer userId
			) {
		ModelAndView mav = new ModelAndView("account/account_update");
		UserModel user = userService.getById(userId);
		Map<String, Object> dataMap = viewBuilder.buildSingleToMap(user, "details", 
				ExplicitViewMapper.getInstance().setViewClass(AdminUserInfoView.class));
		
		mav.addAllObjects(dataMap);
		return mav;
	}
	
	@RequestMapping(value = "/update", method=RequestMethod.POST)
	@ResponseBody
	public Object update(
			@Visitor Integer adminId,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "mobile") String mobile,
			@RequestParam(value = "email") String email
			) {
		adminUserService.updateAdminUser(adminId, name, email, mobile, "");
		
		return JsonResultView.SUCCESS;
	}
	
	
	/**
	 * 跳转到更新密码页面
	 */
	@RequestMapping(value = "/password", method=RequestMethod.GET)
	public String passwordUI() {
		return "account/password";
	}
	
	/**
	 * 更新密码
	 */
	@RequestMapping(value = "/password", method=RequestMethod.POST)
	@ResponseBody
	public Object password(
			@Visitor Integer userId,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "newPassword") String newPassword,
			@RequestParam(value = "confirmPassword") String confirmPassword
			) {
		AdminUserPassport passport = adminUserPassportService.getById(userId);
		if(passport == null) {
			return ZebraMetaCode.ServerFailed;
		}
		
		boolean ok = adminUserPassportService.verifyPassword(password, passport.getPassword());
		if(!ok) {
			return ZebraMetaCode.OldPasswordWrong.toView();
		}
		if(!confirmPassword.equals(newPassword)) {
			return ZebraMetaCode.ServerFailed;
		}
		
		adminUserPassportService.changePassword(userId, newPassword);
		return JsonResultView.SUCCESS;
	}
}
