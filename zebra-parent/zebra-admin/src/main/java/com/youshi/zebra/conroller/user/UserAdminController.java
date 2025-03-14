package com.youshi.zebra.conroller.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.user.constant.UserType;
import com.youshi.zebra.user.service.UserAdminService;
import com.youshi.zebra.user.service.UserService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
/**
 * 
 * 
 * 用户管理，通用接口
 * 
 * @author wangsch
 * @date 2017年2月15日
 */
@RestController
@RequestMapping(value = "/user/admin")
public class UserAdminController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserAdminService userAdminService;
	
	@ApiOperation(value = "封禁用户", tags=SwaggerTags.USER_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/block", method=RequestMethod.POST)
	public Object block(
			@ApiIgnore @Visitor Integer adminId,
			@RequestParam(value = "uid") Integer userId
			) {
		userService.unexpectUserType(userId, UserType.Admin);
		userAdminService.block(adminId, userId);
		
		return JsonResultView.SUCCESS;
	}
	
	@ApiOperation(value = "解除封禁用户", tags=SwaggerTags.USER_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/unblock", method=RequestMethod.POST)
	public Object unblock(
			@ApiIgnore @Visitor Integer adminId,
			@RequestParam(value = "uid") Integer userId) {
		userService.unexpectUserType(userId, UserType.Admin);
		userAdminService.unblock(adminId, userId);
		
		return JsonResultView.SUCCESS;
	}
}