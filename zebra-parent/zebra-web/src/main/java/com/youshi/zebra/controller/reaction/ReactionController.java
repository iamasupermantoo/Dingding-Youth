package com.youshi.zebra.controller.reaction;

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
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.core.web.annotation.LoginRequired;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.reaction.model.StudentReactionModel;
import com.youshi.zebra.reaction.model.TeacherReactionModel;
import com.youshi.zebra.reaction.service.StudentReactionService;
import com.youshi.zebra.reaction.service.TeacherReactionService;
import com.youshi.zebra.view.StudentReactionDetailsView;
import com.youshi.zebra.view.StudentReactionView;
import com.youshi.zebra.view.TeacherReactionDetailsView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * @author wangsch
 * @date 2017年2月23日
 */
@ApiIgnore
@RestController
@RequestMapping(value = "/reaction")
@LoginRequired
public class ReactionController {
	
	@Autowired
	private StudentReactionService studentReactionService;
	
	@Autowired
	private TeacherReactionService teacherReactionService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@ApiOperation(value = "评价列表，分页查询", tags=SwaggerTags.REACTION)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "cursor", name="cursor", dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public Object list(
			@ApiIgnore @Visitor Integer userId,
			@RequestParam(value = "status") Integer status,
			@ApiIgnore @Uuid(value = "cursor", required = false, type=LessonModel.class) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<StudentReactionModel, HasUuid<Integer>> page = studentReactionService
				.getReactions(userId, status, cursor, limit);
		
		JsonResultView result = viewBuilder.build(page, "reactions", 
				ExplicitViewMapper.getInstance().setViewClass(StudentReactionView.class));
		
		return result;
	}
	
	@ApiOperation(value = "学生，提交一个评价", tags=SwaggerTags.REACTION)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "lesson id", name="lid", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/commit", method=RequestMethod.POST)
	public Object commit(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class) Integer courseId,
			@ApiIgnore @Uuid(value = "lid", type=LessonModel.class) Integer lessonId,
			@RequestParam(value = "quality") Integer quality,
			@RequestParam(value = "acceptance") Integer acceptance,
			@RequestParam(value = "remark") String remark,
			@RequestParam(value = "star") Integer star
			) {
		
		studentReactionService.commit(userId, courseId, lessonId, quality, acceptance, remark, star);
		
		return JsonResultView.SUCCESS;
	}
	
	@ApiOperation(value = "评价详情查询(学生评价老师)", tags=SwaggerTags.REACTION)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "lesson id", name="lid", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/details", method=RequestMethod.GET)
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
