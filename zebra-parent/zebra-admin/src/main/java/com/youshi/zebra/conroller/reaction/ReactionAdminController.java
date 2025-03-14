package com.youshi.zebra.conroller.reaction;

import java.util.Map;

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
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.reaction.constants.StudentReactionStatus;
import com.youshi.zebra.reaction.constants.TeacherReactionStatus;
import com.youshi.zebra.reaction.model.StudentReactionModel;
import com.youshi.zebra.reaction.model.TeacherReactionModel;
import com.youshi.zebra.reaction.service.ReactionAdminService;
import com.youshi.zebra.reaction.service.StudentReactionService;
import com.youshi.zebra.reaction.service.TeacherReactionService;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.view.StudentReactionView;
import com.youshi.zebra.view.TeacherReactionView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * @author wangsch
 * @date 2017年8月22日
 */
@Controller
@RequestMapping(value = "/reaction/admin")
public class ReactionAdminController {
	
	
	@Autowired
	private ReactionAdminService reactionAdminService;
	
	@Autowired
	private TeacherReactionService teacherReactionService;
	
	@Autowired
	private StudentReactionService studentReactionService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@RequestMapping(value = "/list/byteacher", method=RequestMethod.GET)
	public ModelAndView byteacher(
			@ApiIgnore @Uuid(value = "teacher", required = false, type=UserModel.class) Integer teacherId,
			@ApiIgnore @Uuid(value = "student", required = false, type=UserModel.class) Integer studentId,
			@ApiIgnore @Uuid(value = "course", required = false, type=CourseModel.class) Integer courseId,
			@RequestParam(value = "status", required=false) TeacherReactionStatus status,
			@ApiIgnore @Uuid(value = "cursor", required = false, type=TeacherReactionService.class) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<TeacherReactionModel, HasUuid<Integer>> page = reactionAdminService
				.getTeacherReactions(teacherId, studentId, courseId, status, cursor, limit);
		
		Map<String, Object> dataMap = viewBuilder.buildToMap(page, "reactions", 
				ExplicitViewMapper.getInstance().setViewClass(TeacherReactionView.class));
		ModelAndView mav = new ModelAndView("reaction/teacher_reaction_list");
		
		mav.addObject("teacher", teacherId);
		mav.addObject("student", studentId);
		mav.addObject("course", courseId);
		mav.addObject("status", status);
		
		return mav.addAllObjects(dataMap);
	}
	
	@RequestMapping(value = "/list/bystudent", method=RequestMethod.GET)
	public ModelAndView bystudent(
			@ApiIgnore @Uuid(value = "teacher", required = false, type=UserModel.class) Integer teacherId,
			@ApiIgnore @Uuid(value = "student", required = false, type=UserModel.class) Integer studentId,
			@ApiIgnore @Uuid(value = "course", required = false, type=CourseModel.class) Integer courseId,
			
			@RequestParam(value = "status", required=false) StudentReactionStatus status,
			@ApiIgnore @Uuid(value = "cursor", required = false, type=TeacherReactionService.class) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<StudentReactionModel, HasUuid<Integer>> page = reactionAdminService
				.getStudentReactions(teacherId, studentId, courseId, status, cursor, limit);
		
		Map<String, Object> dataMap = viewBuilder.buildToMap(page, "reactions", 
				ExplicitViewMapper.getInstance().setViewClass(StudentReactionView.class));
		ModelAndView mav = new ModelAndView("reaction/student_reaction_list");
		
		mav.addObject("teacher", teacherId);
		mav.addObject("student", studentId);
		mav.addObject("course", courseId);
		mav.addObject("status", status);
		
		return mav.addAllObjects(dataMap);
	}
	
	@ApiOperation(value = "评价详情查询(学生评价老师)", tags=SwaggerTags.REACTION)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "lesson id", name="lid", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/details/bystudent", method=RequestMethod.GET)
	@ResponseBody
	public Object details(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class) Integer courseId,
			@ApiIgnore @Uuid(value = "lid", type=LessonModel.class) Integer lessonId) {
		StudentReactionModel reaction = studentReactionService.getOneReaction(userId, courseId, lessonId);
		
		JsonResultView result = viewBuilder.buildSingle(reaction, "reaction", 
				ExplicitViewMapper.getInstance().setViewClass(StudentReactionDetailsView.class));
		
		return result;
	}
	
	@ApiOperation(value = "反馈详情查询(教师给学生)", tags=SwaggerTags.REACTION)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "lesson id", name="lid", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/details/byteacher", method=RequestMethod.GET)
	@ResponseBody
	public Object byteacher(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class) Integer courseId,
			@ApiIgnore @Uuid(value = "lid", type=LessonModel.class) Integer lessonId) {
		TeacherReactionModel reaction = teacherReactionService.getOneReaction(userId, courseId, lessonId);
		
		JsonResultView result = viewBuilder.buildSingle(reaction, "reaction", 
				ExplicitViewMapper.getInstance().setViewClass(TeacherReactionDetailsView.class));
		
		return result;
	}
}
