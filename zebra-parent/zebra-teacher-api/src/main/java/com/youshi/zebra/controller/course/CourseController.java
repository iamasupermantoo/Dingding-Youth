package com.youshi.zebra.controller.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.core.web.annotation.LoginRequired;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.course.service.CourseService;
import com.youshi.zebra.user.constant.UserType;
import com.youshi.zebra.view.CourseView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * @author wangsch
 * @date 2017年1月14日
 */
//@RestController
//@RequestMapping("/course")
public class CourseController {
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@ApiOperation(value = "老师课程列表（老师的每个学生都会有对应一个课程）", response=CourseView.class, tags=SwaggerTags.COURSE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query")
	})
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	@LoginRequired
	public Object list(
			@ApiIgnore @Visitor Integer teacherId
			) {
		PageView<CourseModel, HasUuid<Integer>> page = courseService.getCourses(teacherId, UserType.Teacher);
		
		JsonResultView result = viewBuilder.build(page, "courses", 
				ExplicitViewMapper.getInstance().setViewClass(CourseView.class));
		return result;
	}
}
