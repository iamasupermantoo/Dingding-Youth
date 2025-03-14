package com.youshi.zebra.controller.course;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.course.service.CourseMetaService;
import com.youshi.zebra.teacher.model.TeacherModel;
import com.youshi.zebra.teacher.service.TeacherService;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.view.ImageView;
import com.youshi.zebra.view.TeacherDetailView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * @author wangsch
 * @date 2017年1月14日
 */
@ApiIgnore
@RestController
@Deprecated
@RequestMapping(value = "/teacher")
public class TeacherController {
	
	
	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private CourseMetaService courseMetaService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@ApiOperation(value = "查询老师详细信息", response=TeacherDetailView.class, tags=SwaggerTags.COURSE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "老师id", name="tid", required=true, dataType="String", paramType="query")
	})
	@RequestMapping(value = "/details", method=RequestMethod.GET)
	public Object details(
			@ApiIgnore @Uuid(value = "tid", type=UserModel.class) Integer teacherId
			) {
//		return mockTeacherDetails();
		
		TeacherModel teacher = teacherService.getById(teacherId);
//		List<CourseMetaModel> cms = courseMetaService.getListByIds(teacher.getCmIds());
		
		JsonResultView result = viewBuilder.buildSingle(teacher, "teacher", 
				ExplicitViewMapper.getInstance().setViewClass(TeacherDetailView.class));
		
//		List<Object> courseMetas = viewBuilder.buildToList(cms, 
//				ExplicitViewMapper.getInstance().setViewClass(CourseMetaLiteView.class));
//		
//		result.addValue("courseMetas", courseMetas);
		
		return result;
	}
	
	
	
}
