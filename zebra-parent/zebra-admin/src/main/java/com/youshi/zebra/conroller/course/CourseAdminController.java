package com.youshi.zebra.conroller.course;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.youshi.zebra.course.constants.CourseStatus;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.course.service.CourseAdminService;
import com.youshi.zebra.course.service.CourseService;
import com.youshi.zebra.view.CourseDetailsView;
import com.youshi.zebra.view.CourseView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * 
 * 课程管理
 * 
 * @author wangsch
 * @date 2017年2月18日
 */
@Controller
@RequestMapping("/course/admin")
public class CourseAdminController {
	@Autowired
	private CourseAdminService courseAdminService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@ApiOperation(value = "课程列表查询", tags=SwaggerTags.COURSE_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "cursor", name="cursor", dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public ModelAndView list(
			@ApiIgnore @Visitor Integer userId,
			@RequestParam(value = "cid", required=false) Integer courseId,
			@RequestParam(value = "cmid", required=false) Integer cmId,
			@RequestParam(value = "sid", required=false) Integer studentId,
			@RequestParam(value = "status", required=false) CourseStatus status,
			@ApiIgnore @Uuid(value = "cursor", required = false, type=CourseModel.class) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<CourseModel, HasUuid<Integer>> page = courseAdminService.query(userId, 
				courseId, cmId, studentId, status, cursor, limit);
		
		Map<String, Object> resultMap = viewBuilder.buildToMap(page, "courses", 
				ExplicitViewMapper.getInstance().setViewClass(CourseView.class));
		ModelAndView mav = new ModelAndView("course/course_list");
		mav.addAllObjects(resultMap);
		
		mav.addObject("cid", courseId);
		mav.addObject("cmid", cmId);
		mav.addObject("sid", studentId);
		mav.addObject("status", status);
		
		mav.addObject("cursor", cursor);
		mav.addObject("limit", limit);
		
		return mav;
	}
	
	@ApiOperation(value = "课程详细信息查询", tags=SwaggerTags.COURSE_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/details", method=RequestMethod.GET)
	public Object details(
			@RequestParam(value = "cid") Integer courseId
			) {
		CourseModel course = courseService.getById(courseId);
		JsonResultView result = viewBuilder.buildSingle(course, "course", 
				ExplicitViewMapper.getInstance().setViewClass(CourseDetailsView.class));
		
		return result;
	}
}
