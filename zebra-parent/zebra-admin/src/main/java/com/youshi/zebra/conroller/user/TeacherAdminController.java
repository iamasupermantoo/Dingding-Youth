package com.youshi.zebra.conroller.user;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.teacher.service.TeacherService;
import com.youshi.zebra.user.constant.Gender;
import com.youshi.zebra.user.constant.UserStatus;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.service.UserService;
import com.youshi.zebra.view.TeacherInfoView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * @author wangsch
 * @date 2017年2月24日
 */
@RequestMapping(value = "/teacher/admin")
@RestController
public class TeacherAdminController {
	
	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@RequestMapping(value = "/addUI", method=RequestMethod.GET)
	public ModelAndView addUI() {
		return new ModelAndView("teacher/teacher_add");
	}
	
	@ApiOperation(value = "添加教师", tags=SwaggerTags.TEACHER_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/add", method=RequestMethod.POST)
	public Object add(
			@ApiIgnore @Visitor Integer loginAdminId,
			@RequestParam(value = "mobile") String mobile,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "gender") Gender gender,
			@RequestParam(value = "email", required=false) String email,
			@RequestParam(value = "desc") String desc,
			@RequestParam(value = "age" , required = false) String age,
			@RequestParam(value = "birthday" , required = false) String birthday,
			@RequestParam(value = "headImage", required = false) MultipartFile headImageFile,
			@RequestParam(value = "image", required = false) MultipartFile imageFile,
			HttpServletRequest request
			) {
		
		teacherService.create(mobile, password, name, email, desc, gender.getValue(),birthday , headImageFile, imageFile);
		return JsonResultView.SUCCESS;
	}
	
	@ApiOperation(value = "更新教师信息", tags=SwaggerTags.TEACHER_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/update", method=RequestMethod.POST)
	public Object update(
			@RequestParam(value = "tid") Integer tid,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "desc") String desc,
			@RequestParam(value = "gender") Integer gender,
			@RequestParam(value = "age" , required = false) String age,
			@RequestParam(value = "headImage", required = false) MultipartFile headImageFile,
			@RequestParam(value = "image", required = false) MultipartFile imageFile,
			@RequestParam(value = "cmIds[]") List<Integer> cmIds
			) {
		
		teacherService.update(tid, name, desc, gender,age, headImageFile, imageFile ,cmIds);
		return JsonResultView.SUCCESS;
	}
	
	@RequestMapping(value = "/edit", method=RequestMethod.GET)
	public ModelAndView toEdit(
			@RequestParam(value = "tid") Integer tid
			) {
		ModelAndView modelAndView = new ModelAndView("teacher/teacher_edit");
		UserModel user = userService.getById(tid);
		Map<String, Object> buildSingleToMap = viewBuilder.buildSingleToMap(user, "x", 
				ExplicitViewMapper.getInstance().setViewClass(TeacherInfoView.class));
		modelAndView.addAllObjects(buildSingleToMap);
		return modelAndView;
	}
	
	@ApiOperation(value = "更新教师信息", tags=SwaggerTags.TEACHER_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/edit", method=RequestMethod.POST)
	public Object edit(
			@RequestParam(value = "tid") Integer tid,
			@RequestParam(value = "password", required=false) String password,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "desc") String desc,
			@RequestParam(value = "gender") Integer gender,
			@RequestParam(value = "email", required=false) String email,
			@RequestParam(value = "birthday" , required = false) String birthday,
			@RequestParam(value = "headImage", required = false) MultipartFile headImageFile,
			@RequestParam(value = "image", required = false) MultipartFile imageFile
			) {
		
		teacherService.edit(tid, name, desc, gender, birthday, headImageFile, imageFile);
		return JsonResultView.SUCCESS;
	}
	
	@ApiOperation(value = "教师详细信息", notes="教师详细信息查询", tags=SwaggerTags.TEACHER_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/details", method=RequestMethod.GET)
	public Object details(
			@RequestParam(value = "tid") Integer tid) {
		
		UserModel user = userService.getById(tid);
		JsonResultView result = viewBuilder.buildSingle(user, "details", 
				ExplicitViewMapper.getInstance().setViewClass(TeacherInfoView.class));
		
		return result;
	}
	
	@ApiOperation(value = "教师列表", notes="教师列表，分页查询", tags=SwaggerTags.TEACHER_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "cursor", name="cursor", dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/list")
	public Object list(
			@RequestParam(value = "tid", required=false) Integer teacherId,
			@RequestParam(value = "status", required=false) UserStatus status,
			@ApiIgnore @Uuid(value = "cursor", required = false, type=UserModel.class) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		
		PageView<UserModel, HasUuid<Integer>> page = teacherService.queryTeachers(teacherId, status, cursor, limit);
		Map<String, Object> dataMap = viewBuilder.buildToMap(page, "teachers", 
				ExplicitViewMapper.getInstance().setViewClass(TeacherInfoView.class));
		
		ModelAndView mav = new ModelAndView("teacher/teacher_list");
		return mav.addAllObjects(dataMap);
	}
	
	
	@ApiOperation(value = "教师列表", notes="教师列表，分页查询", tags=SwaggerTags.TEACHER_ADMIN)
	@RequestMapping(value = "/allList")
	@ResponseBody
	public Object allList(
			) {
		PageView<UserModel, HasUuid<Integer>> page = teacherService.queryTeachers(null, null, null, 1000);		
		return page;
	}
	
	
}
