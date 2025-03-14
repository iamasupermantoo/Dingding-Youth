package com.youshi.zebra.conroller.user;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.connect.service.ConnectService;
import com.youshi.zebra.student.service.StudentService;
import com.youshi.zebra.user.constant.UserStatus;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.service.UserService;
import com.youshi.zebra.view.StudentInfoView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * 学生管理
 * 
 * @author wangsch
 * @date 2017年2月24日
 */
@RequestMapping("/student/admin")
@Controller
public class StudentAdminController {
	@Autowired
	private ViewBuilder viewBuilder;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	@Qualifier("mobileConnectService")
	private ConnectService connectService;
	
	@ApiOperation(value = "查看学生列表", tags=SwaggerTags.USER_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "cursor", name="cursor", dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public ModelAndView list(
			@ApiIgnore @Visitor Integer adminId,
			@RequestParam(value = "mobile", required=false) String mobile,
			
			@RequestParam(value = "status", required=false) UserStatus status,
			@ApiIgnore @Uuid(value = "cursor", required = false, type=UserModel.class) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		Integer userId = parseUserId(mobile);
		
		PageView<UserModel, HasUuid<Integer>> page = studentService
				.queryStudents(userId, status, cursor, limit);
		Map<String, Object> resultMap = viewBuilder.buildToMap(page, "users", 
				ExplicitViewMapper.getInstance().setViewClass(StudentInfoView.class));
		
		ModelAndView mav = new ModelAndView("student/student_list");
		mav.addAllObjects(resultMap);
		mav.addObject("mobile", mobile);
		return mav;
	}
	
	@ApiOperation(value = "查看学生详细信息", tags=SwaggerTags.USER_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/details", method=RequestMethod.GET)
	public Object details(
			@ApiIgnore @Visitor Integer adminId,
			@RequestParam(value = "uid") Integer userId) {
		UserModel user = userService.getById(userId);
		JsonResultView result = viewBuilder.buildSingle(user, "details", 
				ExplicitViewMapper.getInstance().setViewClass(StudentInfoView.class));
		
		return result;
	}
	
	private Integer parseUserId(String mobile) {
		Integer userId = null;
		if(StringUtils.isNotEmpty(mobile)) {
			userId = connectService.getUserIdByExternalUserId(mobile);
			if(userId == null) {
				userId = 0;
			}
		}
		return userId;
	}
}
