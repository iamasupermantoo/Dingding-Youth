package com.youshi.zebra.conroller.adminuser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.admin.adminuser.annotation.PrivilegeRequired;
import com.youshi.zebra.admin.adminuser.constant.Privilege;
import com.youshi.zebra.admin.adminuser.model.AdminPrivilegeModel;
import com.youshi.zebra.admin.adminuser.service.AdminUserService;
import com.youshi.zebra.admin.adminuser.service.PrivilegeService;
import com.youshi.zebra.admin.log.annotation.AdminLog;
import com.youshi.zebra.admin.log.constants.AdminLogType;
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.conroller.LoginRequiredController;
import com.youshi.zebra.core.utils.DateTimeUtils;
import com.youshi.zebra.exception.common.ForbiddenException;
import com.youshi.zebra.exception.common.ParamVerifyException;
import com.youshi.zebra.user.constant.UserType;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.service.UserService;
import com.youshi.zebra.view.AdminUserInfoView;
import com.youshi.zebra.view.PrivilegeView;
import com.youshi.zebra.view.PrivilegeView.PrivilegeStatus;

import io.swagger.annotations.ApiOperation;

/**
 * 
 * 
 * 后台管理员相关接口
 * 
 * @author wangsch
 * @date 2017年2月24日
 */
@RequestMapping(value = "/adminuser")
@Controller
@PrivilegeRequired({ Privilege.AdminUserAdmin })
public class AdminUserController extends LoginRequiredController {
    @Autowired
    private AdminUserService adminUserService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ViewBuilder viewBuilder;
    
    @Autowired
    private PrivilegeService privilegeService;
    
    @ApiOperation(value = "查询后台用户")
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public ModelAndView list(
			@Uuid(value = "limit", type=UserModel.class, required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		ModelAndView mav = new ModelAndView("adminuser/admin_user_list");
		
        PageView<UserModel, HasUuid<Integer>> page = adminUserService.getAdminUsers(cursor, limit);
        Map<String, Object> result = viewBuilder.buildToMap(page, "users",
        		ExplicitViewMapper.getInstance().setViewClass(AdminUserInfoView.class));
        
        mav.addAllObjects(result);
        return mav;
	}
	
	@ApiOperation(value = "封禁后台用户")
    @RequestMapping(value = "/block", method=RequestMethod.POST)
	@ResponseBody
    public Object block(
    		@Visitor Integer loginAdminId,
    		@RequestParam(value = "aid") Integer userId) {
		checkUserType(loginAdminId, userId);
		
    	adminUserService.blockAdminUser(loginAdminId, userId);
        
        return JsonResultView.SUCCESS;
    }
	
	@ApiOperation(value = "解除封禁后台用户")
    @RequestMapping(value = "/unblock", method=RequestMethod.POST)
	@ResponseBody
	public Object unblock(
			@Visitor Integer loginAdminId,
    		@RequestParam(value = "aid") Integer userId) {
		checkUserType(loginAdminId, userId);
		
		adminUserService.unblockAdminUser(loginAdminId, userId);
        
        return JsonResultView.SUCCESS;
	}
	
	@ApiOperation(value = "查看后台用户详细信息", tags=SwaggerTags.ADMIN_USER)
    @RequestMapping(value = "/details", method=RequestMethod.GET)
	public ModelAndView details(
			@RequestParam(value = "aid") Integer adminId
			) {
		ModelAndView mav = new ModelAndView("adminuser/admin_user_details");
		UserModel user = userService.getById(adminId);
		
		Map<String, Object> map = viewBuilder.buildSingleToMap(user, "details", 
				ExplicitViewMapper.getInstance().setViewClass(AdminUserInfoView.class));
		
		return mav.addAllObjects(map);
	}
	
	@RequestMapping(value = "/privilege/list", method = RequestMethod.GET)
	public ModelAndView privilegeList(
			@RequestParam(value = "aid") Integer adminId
			) {
		UserModel user = userService.getById(adminId);
		Map<String, Object> dataMap = viewBuilder.buildSingleToMap(user, "user", 
				ExplicitViewMapper.getInstance().setViewClass(AdminUserInfoView.class));
		
		Map<Privilege, AdminPrivilegeModel> privileges = privilegeService.getPrivileges(adminId);
		
		List<PrivilegeView> privilegeViews = buildResult(privileges);
		dataMap.put("privileges", privilegeViews)
		;
		return new ModelAndView("adminuser/admin_user_privileges").addAllObjects(dataMap);
	}
	
	@RequestMapping(value = "/privilege/update", method = RequestMethod.POST)
	@ResponseBody
	public Object update(
			@RequestParam(value = "aid") Integer adminId,
			@RequestParam(value = "privileges") String[] rawPrivileges,
			@RequestParam(value = "expireTimes") String[] expireTimes
			) {
		Map<Privilege, Long> privileges = parsePrivilege(rawPrivileges, expireTimes);
		privilegeService.updatePrivileges(adminId, privileges);
		
		return JsonResultView.SUCCESS;
	}
	
	private Map<Privilege, Long> parsePrivilege(String[] rawPrivileges, String[] expireTimes) {
		if(rawPrivileges.length != expireTimes.length) {
			throw new ParamVerifyException();
		}
		Map<Privilege, Long> result = new HashMap<>();
		for (int i=0; i<rawPrivileges.length; i++) {
			if(StringUtils.isEmpty(expireTimes[i])) {
				continue;
			}
			Privilege key = Privilege.valueOf(rawPrivileges[i]);
			Long value = DateTimeUtils.parseDate(expireTimes[i]);
			result.put(key, value);
		}
		
		return result;
	}
	
	private List<PrivilegeView> buildResult(Map<Privilege, AdminPrivilegeModel> privileges) {
		List<PrivilegeView> result = new ArrayList<>();
		for(Entry<Privilege, AdminPrivilegeModel> entry : privileges.entrySet()) {
			Privilege pri = entry.getKey();
			AdminPrivilegeModel privilege = entry.getValue();
			if(privilege == null) {
				result.add(new PrivilegeView(false, 
						pri, PrivilegeStatus.NONE));
			} else {
				PrivilegeStatus status = System.currentTimeMillis() > privilege.getExpireTime() ?
						PrivilegeStatus.EXPIRED : PrivilegeStatus.NORMAL;
				result.add(new PrivilegeView(privilege.getId(), false, 
						pri, privilege.getExpireTime(), status));
			}
		}
		return result;
	}
	
	@RequestMapping(value = "/addUI", method=RequestMethod.GET)
	public String addUI() {
		return "adminuser/admin_user_add";
	}
	
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@AdminLog(AdminLogType.AdminUserAdd)
	@ResponseBody
	public Object add(
			@RequestParam(value = "name") String name,
			@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "signature", required=false) String signature,
			@RequestParam(value = "email", required=false) String email,
			@RequestParam(value = "mobile", required=false) String mobile,
			@RequestParam(value = "remark", required=false) String remark,
			HttpServletRequest request, HttpServletResponse response) {
		
		adminUserService.addAdminUser(name, username, password, email, mobile, remark);
		return JsonResultView.SUCCESS;
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@AdminLog(AdminLogType.AdminUserUpdate)
	@ResponseBody
	public Object update(
			@RequestParam(value = "aid") Integer adminId,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "email", required=false) String email,
			@RequestParam(value = "mobile", required=false) String mobile,
			@RequestParam(value = "remark", required=false) String remark) {
		adminUserService.updateAdminUser(adminId, name, email, mobile, remark);
		
		return JsonResultView.SUCCESS;
	}
	

	/**
	 * @param loginAdminId
	 * @param userId
	 */
	private void checkUserType(Integer loginAdminId, Integer userId) {
		UserModel user = userService.getById(userId);
		UserType type = UserType.fromValue(user.getType());
		if(type != UserType.Admin) {
			logger.error("User type is not ADMIN. loginAdminId: {}, uid: {}, userType: {}", 
					loginAdminId, userId, type);
			throw new ForbiddenException();
		}
	}
}
