package com.youshi.zebra.controller.homework;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
@ApiIgnore
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
	
	@ApiOperation(value = "作业列表", notes="作业列表，分页查询", tags=SwaggerTags.HOMEWORK)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "cursor", name="cursor", dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public Object list(
			@ApiIgnore @Visitor Integer studentId,
			@RequestParam(value = "status", required=false) Integer status,
			@ApiIgnore @Uuid(value = "cursor", required = false, type=HomeworkModel.class) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<HomeworkModel, HasUuid<Integer>> homeworks = homeworkService
				.getStudentHomeworks(studentId, status, cursor, limit);
		
		JsonResultView result = viewBuilder.build(homeworks, "homeworks", 
				ExplicitViewMapper.getInstance().setViewClass(HomeworkView.class));
		
		int waitCommit = homeworkService.getWaitCommit(studentId);
		result.addValue("waitCommit", waitCommit);
		
		return result;
	}
	
	@ApiOperation(value = "作业详情", notes="查询作业详情，返回作业信息+答案", tags=SwaggerTags.HOMEWORK)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "作业id", name="hid", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/details", method=RequestMethod.GET)
	public Object details(
			@ApiIgnore @Visitor Integer userId,
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
	
	// ---------------------------- 答案相关 --------------------------------
	@ApiOperation(value = "提交一条答案", notes="支持三种类型，文本、图片、语音", tags=SwaggerTags.HOMEWORK)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "作业id", name="hid", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/answer/commit", method=RequestMethod.POST)
	public Object commitAnswer(
			@ApiIgnore @Visitor Integer studentId,
			@ApiIgnore @Uuid(value = "hid", type=HomeworkModel.class) Integer homeworkId,
			@ApiParam(value = "类型，0:文本，1:图片，2:语音", required = true)
			@RequestParam(value = "type") Integer type,
			@ApiParam(value = "文本")
			@RequestParam(value = "text", required=false) String text,
			@ApiParam(value = "图片文件集合（最多三个）")
			@RequestParam(value = "images[]", required=false) MultipartFile[] images
			,
			@ApiParam(value = "语音文件")
			@RequestParam(value = "audio", required=false) MultipartFile audio
			) {
		
		HomeworkAnswerModel answer = homeworkAnswerService.addAnswer(studentId, homeworkId, 
				type, text, images, audio);
		JsonResultView result = viewBuilder.buildSingle(answer, "answer", 
				ExplicitViewMapper.getInstance().setViewClass(HomeworkAnswerView.class));
		return result;
	}
	
	
	@ApiOperation(value = "删除一条答案", tags=SwaggerTags.HOMEWORK)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "作业id", name="hid", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "答案id", name="aid", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/answer/remove", method=RequestMethod.POST)
	public Object removeAnswer(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "hid", type=HomeworkModel.class) Integer homeworkId,
			@ApiIgnore @Uuid(value = "aid", type=HomeworkAnswerModel.class) Integer answerId
			) {
		homeworkAnswerService.removeAnswer(userId, homeworkId, answerId);
		return JsonResultView.SUCCESS;
	}
}
