package com.youshi.zebra.conroller.lesson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.crud.model.util.UuidUtils;
import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.lesson.dao.LessonPlanService;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.view.LessonPlanView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 中心排课Controller
 * 
 * @author wangsch
 * @date 2017年3月24日
 */
@RestController
@RequestMapping(value = "/lesson/center/plan")
public class LessonCenterPlanController {
	@Autowired
	private LessonPlanService lessonPlanService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@ApiOperation(value = "查询某个课程下的lesson，只查询已排课的lesson", tags=SwaggerTags.LESSON_PLAN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public Object list(
			@ApiIgnore @Visitor Integer userId,
//			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class) Integer courseId,
			@RequestParam(value = "cid") Integer courseId,
			@RequestParam(value = "cursor", required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		
		PageView<LessonModel, HasUuid<Integer>> page = lessonPlanService
				.getPlanedLessons(userId, courseId, cursor, limit);
		
		JsonResultView result = viewBuilder.build(page, "lessons", 
				ExplicitViewMapper.getInstance().setViewClass(LessonPlanView.class));
		
		return result;
	}
	
	@ApiOperation(value = "修改一个lesson，主要是填写老师", tags=SwaggerTags.LESSON_PLAN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/modify", method=RequestMethod.POST)
	public Object modify(
			@ApiIgnore @Visitor Integer userId,
//			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class) Integer courseId,
//			@ApiIgnore @Uuid(value = "lid", type=LessonModel.class) Integer lessonId,
//			@ApiIgnore @Uuid(value = "tid", type=UserModel.class) Integer teacherId,
//			@ApiIgnore @Uuid(value = "aid", type=UserModel.class) Integer assistantId,
			@RequestParam(value = "cid") Integer courseId,
			@RequestParam(value = "lid") Integer lessonId,
			@RequestParam(value = "tid") Integer teacherId
			) {
		
		
		return JsonResultView.SUCCESS;
	}
}
