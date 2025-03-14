package com.youshi.zebra.controller.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.ZebraCommonApiTags;
import com.youshi.zebra.student.model.TeacherStudentModel;
import com.youshi.zebra.student.service.TeacherStudentService;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.view.TeacherStudentView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * @author wangsch
 * @date 2017年2月21日
 */
//@RestController
//@RequestMapping("/student")
public class StudentController {
	@Autowired
	private ViewBuilder viewBuilder;
	
	@Autowired
	private TeacherStudentService teacherStudentService;
	
	@ApiOperation(value = "学生列表", notes="学生列表，分页查询", tags=ZebraCommonApiTags.MINE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "cursor", name="cursor", dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public Object list(
			@ApiIgnore @Visitor Integer teacherId,
			@ApiIgnore @Uuid(value = "cursor", required = false, type=UserModel.class) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<TeacherStudentModel, HasUuid<Integer>> students = teacherStudentService
				.getStudents(teacherId, cursor, limit);
		
		JsonResultView result = viewBuilder.build(students, "students", 
				ExplicitViewMapper.getInstance().setViewClass(TeacherStudentView.class));
		return result;
	}
	
	
}
