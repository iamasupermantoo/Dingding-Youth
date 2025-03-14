package com.youshi.zebra.conroller.homework;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.conroller.LoginRequiredController;
import com.youshi.zebra.homework.constants.HomeworkStatus;
import com.youshi.zebra.homework.model.HomeworkAnswerModel;
import com.youshi.zebra.homework.model.HomeworkModel;
import com.youshi.zebra.homework.service.HomeworkAdminService;
import com.youshi.zebra.homework.service.HomeworkAnswerService;
import com.youshi.zebra.homework.service.HomeworkService;
import com.youshi.zebra.view.HomeworkAnswerView;
import com.youshi.zebra.view.HomeworkDetailsView;
import com.youshi.zebra.view.HomeworkView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * 作业管理Controller
 * 
 * @author wangsch
 * @date 2017年3月3日
 */
@RestController
@RequestMapping(value = "/homework/admin")
public class HomeworkAdminController extends LoginRequiredController {
	
	@Autowired
	private HomeworkAdminService homeworkAdminService;
	
	@Autowired
	private HomeworkService homeworkService;
	
	@Autowired
	private HomeworkAnswerService homeworkAnswerService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@ApiOperation(value = "作业列表", notes="作业列表，分页查询", tags=SwaggerTags.HOMEWORK_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "cursor", name="cursor", dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public ModelAndView list(
			@ApiIgnore @Visitor Integer loginAdminId,
			@RequestParam(value = "cid", required=false) Integer courseId,
			@RequestParam(value = "lid", required=false) Integer lessonId,
			@RequestParam(value = "tid", required=false) Integer teacherId,
			@RequestParam(value = "sid", required=false) Integer studentId,
			@RequestParam(value = "status", required=false) HomeworkStatus status,
			@ApiIgnore @Uuid(value = "cursor", required = false, type=HomeworkModel.class) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<HomeworkModel, HasUuid<Integer>> homeworks = homeworkAdminService
				.queryHomeworks(courseId, lessonId, teacherId, studentId, status, cursor, limit);
		
		Map<String, Object> resultMap = viewBuilder.buildToMap(homeworks, "homeworks", 
				ExplicitViewMapper.getInstance().setViewClass(HomeworkView.class));
		ModelAndView mav = new ModelAndView("homework/homework_list");
		
		mav.addObject("cid", courseId);
		mav.addObject("lid", lessonId);
		mav.addObject("tid", teacherId);
		mav.addObject("sid", studentId);
		mav.addObject("status", status);
		
		return mav.addAllObjects(resultMap);
	}
	
	
	@ApiOperation(value = "作业详情", notes="查询作业详情，返回作业信息+答案", tags=SwaggerTags.HOMEWORK_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "作业id", name="hid", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/details", method=RequestMethod.GET)
	public ModelAndView details(
			@ApiIgnore @Visitor Integer loginAdminId,
//			@RequestParam(value = "cid") Integer courseId,
//			@RequestParam(value = "lid") Integer lessonId,
			@RequestParam(value = "hid") Integer homeworkId
			) {
		HomeworkModel homework = homeworkService.getById(homeworkId);
		List<HomeworkAnswerModel> answersModel = homeworkAnswerService.getHomeworkAnswers(homeworkId);
		
		Map<String, Object> resultMap = viewBuilder.buildSingleToMap(homework, "details", 
				ExplicitViewMapper.getInstance().setViewClass(HomeworkDetailsView.class));
		List<Object> answers = viewBuilder.buildToList(answersModel, 
				ExplicitViewMapper.getInstance().setViewClass(HomeworkAnswerView.class));
		
		resultMap.put("answers", answers);
		ModelAndView mav = new ModelAndView("homework/homework_details");
		
		return mav.addAllObjects(resultMap);
	}
	
	
}
