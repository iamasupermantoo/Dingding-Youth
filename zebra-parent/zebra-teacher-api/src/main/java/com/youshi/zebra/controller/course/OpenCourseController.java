package com.youshi.zebra.controller.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.core.constants.config.RawStringConfigKey;
import com.youshi.zebra.course.constants.CourseStatus;
import com.youshi.zebra.course.model.LiveMetaModel;
import com.youshi.zebra.course.service.LiveMetaService;
import com.youshi.zebra.recommend.model.RecommendFeedView;
import com.youshi.zebra.view.CourseMetaDetailsView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 公开课
 * @author guo
 *
 */
@RestController
@RequestMapping("/opencourse")
public class OpenCourseController {
	
	@Autowired
	private LiveMetaService liveMetaService;
	
	
	
	@ApiOperation(value = "公开课列表", response=CourseMetaDetailsView.class, tags=SwaggerTags.COURSE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "cursor", name="cursor", dataType="String", required=false, paramType="query"),
	})
	@RequestMapping(value = "/meta/list", method=RequestMethod.GET)
	public Object metaList(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "cursor", type = LiveMetaModel.class, required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<RecommendFeedView, String> opencourses = liveMetaService.getTeacherCourseList(userId, cursor, limit);
		JsonResultView result = new JsonResultView();
		result.addValue("opencourses", opencourses );
		result.addValue("phone", RawStringConfigKey.CoursePhone.defaultValue());
		return result;
	}
	
	@ApiOperation(value = "结束上课", response=CourseMetaDetailsView.class, tags=SwaggerTags.COURSE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "课程meta id", name="lmid", required=true, dataType="String", paramType="query")
	})
	@RequestMapping(value = "/meta/end", method=RequestMethod.GET)
	public Object end(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "lmid", type=LiveMetaModel.class) Integer lmId
			) {
		liveMetaService.updateStatus(lmId, CourseStatus.Finished);
		return JsonResultView.SUCCESS;
	}
	
	@ApiOperation(value = "开始上课", response=CourseMetaDetailsView.class, tags=SwaggerTags.COURSE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "课程meta id", name="lmid", required=true, dataType="String", paramType="query")
	})
	@RequestMapping(value = "/meta/start", method=RequestMethod.GET)
	public Object start(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "lmid", type=LiveMetaModel.class) Integer lmId
			) {
		liveMetaService.updateStatus(lmId, CourseStatus.OnProgress);
		return JsonResultView.SUCCESS;
	}
	
	
/*	@ApiOperation(value = "已购公开课列表", response=CourseMetaDetailsView.class, tags=SwaggerTags.COURSE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "cursor", name="cursor", dataType="String", required=false, paramType="query"),
	})
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public Object list(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "cursor", type = LiveModel.class, required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<RecommendFeedView, String> opencourses = liveMetaService.getPaidOpenCourseList(userId, cursor, limit);
		JsonResultView result = new JsonResultView();
		
		result.addValue("opencourses", opencourses);
		result.addValue("phone", RawStringConfigKey.CoursePhone.defaultValue());
		
		return result;
	}*/
	
	
	
	
	
}
