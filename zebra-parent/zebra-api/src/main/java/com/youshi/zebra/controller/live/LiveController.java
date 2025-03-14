package com.youshi.zebra.controller.live;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.framework.utils.DoradoMapperUtils;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.course.model.LiveMetaModel;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.live.model.LiveInfo;
import com.youshi.zebra.live.service.LiveService;
import com.youshi.zebra.view.LiveFrameInfoView;
import com.youshi.zebra.view.LiveInfoView;
import com.youshi.zebra.view.LiveRejoinInfoView;
import com.youshi.zebra.view.LiveRoomInfoView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * 
 * 
 * 
 * @author wangsch
 * @date 2017年3月13日
 */
@RequestMapping(value = "/live")
@RestController
public class LiveController {
	private static final Logger logger = LoggerFactory.getLogger(LiveController.class);
	
	@Autowired
	private LiveService liveService;
	
	@ApiOperation(value = "加入房间，需要的信息", response=LiveInfoView.class, tags=SwaggerTags.LIVE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "公开课id", name="lm", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/opencourse/info", method=RequestMethod.GET)
	public Object opencourseinfo(
			@ApiIgnore @Visitor Integer studentId,
			@ApiIgnore @Uuid(value = "lm", type=LiveMetaModel.class) Integer lmId
			) {
		LiveInfo liveInfo = liveService.getStudentOpenCourseLiveInfo(studentId, lmId);
		LiveInfoView liveInfoView = openCourseWrapView(liveInfo);
		
		JsonResultView result = new JsonResultView()
				.addValue("liveInfo", liveInfoView);
		
		logger.info("Fuck student opencourse live info. result: " + DoradoMapperUtils.toJSON(result));
		
		return result;
	}
	
	@ApiOperation(value = "加入房间，需要的信息", response=LiveInfoView.class, tags=SwaggerTags.LIVE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "lesson id", name="lid", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/info", method=RequestMethod.GET)
	public Object info(
			@ApiIgnore @Visitor Integer studentId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class) Integer courseId,
			@ApiIgnore @Uuid(value = "lid", type=LessonModel.class) Integer lessonId
			) {
		LiveInfo liveInfo = liveService.getStudentLiveInfo(studentId, courseId, lessonId, 
				WebRequestContext.getAppPlatform());
		LiveInfoView liveInfoView = wrapView(liveInfo);
		
		JsonResultView result = new JsonResultView()
				.addValue("liveInfo", liveInfoView);
		
		logger.info("Fuck student live info. result: " + DoradoMapperUtils.toJSON(result));
		
		return result;
	}

	
	public Object parentJoin() {
		return null;
	}
	
	@ApiOperation(value = "加入直播课堂", tags=SwaggerTags.LIVE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "lesson id", name="lid", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/join", method=RequestMethod.POST)
	@Deprecated
	public Object join(
			@ApiIgnore @Visitor Integer studentId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class) Integer courseId,
			@ApiIgnore @Uuid(value = "lid", type=LessonModel.class) Integer lessonId
			) {
		liveService.studentJoin(studentId, courseId, lessonId, 
				WebRequestContext.getAppPlatform());
		return JsonResultView.SUCCESS;
	}
	
	@ApiOperation(value = "离开直播", tags=SwaggerTags.LIVE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "lesson id", name="lid", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/leave", method=RequestMethod.POST)
	public Object leave(
			@ApiIgnore @Visitor Integer studentId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class) Integer courseId,
			@ApiIgnore @Uuid(value = "lid", type=LessonModel.class) Integer lessonId
			) {
		liveService.studentLeave(studentId, courseId, lessonId);
		
		return JsonResultView.SUCCESS;
	}
	
	
	private LiveInfoView openCourseWrapView(LiveInfo liveInfo) {
		LiveRoomInfoView roomInfo = new LiveRoomInfoView();
		roomInfo.setChannelName(liveInfo.getChannelName());
		roomInfo.setChannelKey(liveInfo.getChannelKey());
		roomInfo.setSignalingKey(liveInfo.getSignalingKey());
		roomInfo.setAccount(liveInfo.getAccount());
		roomInfo.setTeacherAccount(liveInfo.getTeacherAccount());
		roomInfo.setRole(liveInfo.getRole());
		
		LiveInfoView liveInfoView = new LiveInfoView(roomInfo, null, null);
		return liveInfoView;
	}
	
	private LiveInfoView wrapView(LiveInfo liveInfo) {
		LiveRoomInfoView roomInfo = new LiveRoomInfoView();
		roomInfo.setChannelName(liveInfo.getChannelName());
		roomInfo.setChannelKey(liveInfo.getChannelKey());
		roomInfo.setSignalingKey(liveInfo.getSignalingKey());
		roomInfo.setAccount(liveInfo.getAccount());
		roomInfo.setStudentAccount(liveInfo.getStudentAccount());
		roomInfo.setTeacherAccount(liveInfo.getTeacherAccount());
		roomInfo.setRole(liveInfo.getRole());
		
		LiveRejoinInfoView rejoinInfo = new LiveRejoinInfoView(liveInfo.getCurrentFrameX(), 
				liveInfo.getCurrentFrameY());
		
		
		LiveFrameInfoView framesInfoView = DoradoMapperUtils.fromJSON(liveInfo.getFramesInfo(), LiveFrameInfoView.class);
		
		LiveInfoView liveInfoView = new LiveInfoView(roomInfo, framesInfoView, rejoinInfo);
		return liveInfoView;
	}
}
