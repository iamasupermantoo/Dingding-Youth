package com.youshi.zebra.controller.homework;

import java.util.List;

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
import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.core.web.annotation.LoginRequired;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.homework.model.HomeworkAnswerModel;
import com.youshi.zebra.homework.model.HomeworkModel;
import com.youshi.zebra.homework.service.HomeworkAnswerService;
import com.youshi.zebra.homework.service.HomeworkService;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.view.HomeworkAnswerView;
import com.youshi.zebra.view.HomeworkDetailsView;
import com.youshi.zebra.view.HomeworkView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * 
 * 作业相关
 * 
 * @author wangsch
 * @date 2017年2月6日
 */
@RestController
@RequestMapping("/homework")
@LoginRequired
public class HomeworkController {

	@Autowired
	private HomeworkService homeworkService;
	
	@Autowired
	private HomeworkAnswerService homeworkAnswerService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@ApiOperation(value = "作业列表", notes="作业列表，分页查询。", tags=SwaggerTags.HOMEWORK)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "学生id", name="sid", dataType="String", paramType="query"),
		@ApiImplicitParam(value = "cursor", name="cursor", dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public Object list(
			@ApiIgnore @Visitor Integer teacherId,
			@ApiIgnore @Uuid(value = "sid", type=UserModel.class, required=false) Integer studentId,
			@ApiParam(value = "状态")
			@RequestParam(value = "status", required=false) Integer status,
			@ApiIgnore @Uuid(value = "cursor", required = false, type=HomeworkModel.class) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<HomeworkModel, HasUuid<Integer>> homeworks = homeworkService
				.getTeacherHomeworks(teacherId, studentId, status, cursor, limit);
		
		JsonResultView result = viewBuilder.build(homeworks, "homeworks", 
				ExplicitViewMapper.getInstance().setViewClass(HomeworkView.class));
		return result;
	}
	
	@ApiOperation(value = "作业详情", notes="查询作业详情，返回作业信息+答案", tags=SwaggerTags.HOMEWORK)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", required=false, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "lesson id", name="lid", required=false, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "作业id", name="hid", required=false, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/details", method=RequestMethod.GET)
	public Object details(
			@ApiIgnore @Uuid(value = "hid", type=HomeworkModel.class, required=false) Integer homeworkId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class, required=false) Integer courseId,
			@ApiIgnore @Uuid(value = "lid", type=LessonModel.class, required=false) Integer lessonId
			) {
		HomeworkModel homework = homeworkService.getHomework(homeworkId, courseId, lessonId);
		if(homework == null) {
			return ZebraMetaCode.EntityNotFound.toView();
		}
		List<HomeworkAnswerModel> answersModel = homeworkAnswerService.getHomeworkAnswers(homework.getId());
		
		JsonResultView result = viewBuilder.buildSingle(homework, "details", 
				ExplicitViewMapper.getInstance().setViewClass(HomeworkDetailsView.class));
		List<Object> answers = viewBuilder.buildToList(answersModel, 
				ExplicitViewMapper.getInstance().setViewClass(HomeworkAnswerView.class));
		
		result.addValue("answers", answers);
		return result;
	}
	
	@ApiOperation(value = "批改作业", tags=SwaggerTags.HOMEWORK)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "作业id", name="hid", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/correct", method=RequestMethod.POST)
	public Object correct(
			@ApiIgnore @Visitor Integer teacherId,
			@ApiIgnore @Uuid(value = "hid", type=HomeworkModel.class) Integer homeworkId,
			
			@ApiParam(value = "是否按时。true：按时，false：未按时", required=true)
			@RequestParam(value = "intime") Boolean intime,
			
			@ApiParam(value = "完成度，数字：0 - 4", required=true)
			@RequestParam(value = "complete") Integer complete,
			
			@ApiParam(value = "完成质量，数字：0 - 4", required=true)
			@RequestParam(value = "quality") Integer quality,
			
			@ApiParam(value = "教师评语", required=false)
			@RequestParam(value = "remark", required=false) String remark
			) {
		homeworkService.correct(teacherId, homeworkId, intime, complete, quality, remark);
		
		return JsonResultView.SUCCESS;
	}
}
