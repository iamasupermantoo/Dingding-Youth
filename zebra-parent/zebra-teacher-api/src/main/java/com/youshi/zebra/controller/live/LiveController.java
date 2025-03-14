package com.youshi.zebra.controller.live;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.course.model.LiveMetaModel;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.live.model.LiveInfo;
import com.youshi.zebra.live.model.OssSdkInfo;
import com.youshi.zebra.live.service.LiveService;
import com.youshi.zebra.view.LiveFrameInfoView;
import com.youshi.zebra.view.LiveInfoView;
import com.youshi.zebra.view.LiveRejoinInfoView;
import com.youshi.zebra.view.LiveRoomInfoView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
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
			@ApiIgnore @Visitor Integer teacherId,
			@ApiIgnore @Uuid(value = "lm", type=LiveMetaModel.class) Integer lmId
			) {
		LiveInfo liveInfo = liveService.getTeacherOpenCourseLiveInfo(teacherId, lmId);
		LiveInfoView liveInfoView = openCourseWrapView(liveInfo);
		
		JsonResultView result = new JsonResultView()
				.addValue("liveInfo", liveInfoView);
		
		logger.info("Fuck teacher opencourse live info. result: " + DoradoMapperUtils.toJSON(result));
		
		return result;
	}
	
	@ApiOperation(value = "加入房间，需要的信息", response=LiveInfoView.class, tags=SwaggerTags.LIVE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "lesson id", name="lid", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/info", method=RequestMethod.GET)
	public Object info(
			@ApiIgnore @Visitor Integer studentId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class) Integer courseId,
			@ApiIgnore @Uuid(value = "lid", type=LessonModel.class) Integer lessonId
			) {
		LiveInfo liveInfo = liveService.getTeacherLiveInfo(studentId, courseId, lessonId);
		
		LiveInfoView liveInfoView = wrapView(liveInfo);
		
		JsonResultView result = new JsonResultView()
				.addValue("liveInfo", liveInfoView);
		
		return result;
	}
	
	@ApiOperation(value = "获取oss sdk需要的参数信息，用于上传录制的视频", tags=SwaggerTags.LIVE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "lesson id", name="lid", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/oss", method=RequestMethod.GET)
	public Object oss(
			@ApiIgnore @Visitor Integer teacherId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class) Integer courseId,
			@ApiIgnore @Uuid(value = "lid", type=LessonModel.class) Integer lessonId
			) {
		OssSdkInfo info = liveService.getOssSdkInfo(teacherId, courseId, lessonId);
		
		return new JsonResultView().addValue("info", info);
	}
	
	@ApiOperation(value = "上传视频成功后调用", tags=SwaggerTags.LIVE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "lesson id", name="lid", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/uploadok", method=RequestMethod.POST)
	public Object uploadok(
			@ApiIgnore @Visitor Integer teacherId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class) Integer courseId,
			@ApiIgnore @Uuid(value = "lid", type=LessonModel.class) Integer lessonId
			) {
		liveService.uploadOK(teacherId, courseId, lessonId);
		
		return JsonResultView.SUCCESS;
	}
	
	@ApiOperation(value = "准备上课", tags=SwaggerTags.LIVE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "lesson id", name="lid", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/prepare", method=RequestMethod.POST)
	public Object prepare(
			@ApiIgnore @Visitor Integer teacherId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class) Integer courseId,
			@ApiIgnore @Uuid(value = "lid", type=LessonModel.class) Integer lessonId
			) {
		liveService.prepare(teacherId, courseId, lessonId);
		
		return JsonResultView.SUCCESS;
	}
	
	@ApiOperation(value = "离开直播（准备前或者准备后离开，调用这个接口）", tags=SwaggerTags.LIVE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "lesson id", name="lid", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/leave", method=RequestMethod.POST)
	public Object leave(
			@ApiIgnore @Visitor Integer teacherId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class) Integer courseId,
			@ApiIgnore @Uuid(value = "lid", type=LessonModel.class) Integer lessonId
			) {
		liveService.teacherLeave(teacherId, courseId, lessonId);
		
		return JsonResultView.SUCCESS;
	}

	@ApiOperation(value = "开始上课", tags=SwaggerTags.LIVE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "lesson id", name="lid", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/start", method=RequestMethod.POST)
	public Object start(
			@ApiIgnore @Visitor Integer teacherId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class) Integer courseId,
			@ApiIgnore @Uuid(value = "lid", type=LessonModel.class) Integer lessonId
			) {
		liveService.start(teacherId, courseId, lessonId);
		
		return JsonResultView.SUCCESS;
	}
	
	@ApiOperation(value = "结束上课", tags=SwaggerTags.LIVE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "lesson id", name="lid", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/stop", method=RequestMethod.POST)
	public Object stop(
			@ApiIgnore @Visitor Integer teacherId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class) Integer courseId,
			@ApiIgnore @Uuid(value = "lid", type=LessonModel.class) Integer lessonId
			) {
		liveService.stop(teacherId, courseId, lessonId);
		
		return JsonResultView.SUCCESS;
	}
	
	@ApiOperation(value = "修改当前帧", notes="老师上课时，点击前进、后退按钮时，调用这个方法。", tags=SwaggerTags.LIVE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "lesson id", name="lid", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/changeframe", method=RequestMethod.POST)
	public Object changeframe(
			@ApiIgnore @Visitor Integer teacherId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class) Integer courseId,
			@ApiIgnore @Uuid(value = "lid", type=LessonModel.class) Integer lessonId,
			@ApiParam(value = "x坐标", required=true)
			@RequestParam(value = "frameX") Integer frameX,
			@ApiParam(value = "y坐标", required=true)
			@RequestParam(value = "frameY") Integer frameY
			) {
		liveService.changeframe(teacherId, courseId, lessonId, frameX, frameY);
		
		return JsonResultView.SUCCESS;
	}
	
	@ApiIgnore
	@ApiOperation(value = "在屏幕上做标记", tags=SwaggerTags.LIVE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "lesson id", name="lid", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/screen/mark", method=RequestMethod.POST)
	@Deprecated
	public Object mark(
			@ApiIgnore @Visitor Integer teacherId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class) Integer courseId,
			@ApiIgnore @Uuid(value = "lid", type=LessonModel.class) Integer lessonId,
			@RequestParam(value = "line") String line
			) {
		
		return JsonResultView.SUCCESS;
	}
	
	@ApiIgnore
	@ApiOperation(value = "清屏", tags=SwaggerTags.LIVE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "lesson id", name="lid", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/screen/clear", method=RequestMethod.POST)
	@Deprecated
	public Object clear(
			@ApiIgnore @Visitor Integer teacherId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class) Integer courseId,
			@ApiIgnore @Uuid(value = "lid", type=LessonModel.class) Integer lessonId
			) {
		return JsonResultView.SUCCESS;
	}
	
	// TODO 老师送礼物，后台需要记录，等待pc端加这个功能的时候，再实现这个接口
	@ApiOperation(value = "送星星", tags=SwaggerTags.LIVE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "lesson id", name="lid", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/gift/send", method=RequestMethod.POST)
	public Object star(
			@ApiIgnore @Visitor Integer teacherId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class) Integer courseId,
			@ApiIgnore @Uuid(value = "lid", type=LessonModel.class) Integer lessonId
			) {
		
		
		
		
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
		roomInfo.setAppId(liveInfo.getAppId());
		roomInfo.setChannelName(liveInfo.getChannelName());
		roomInfo.setChannelKey(liveInfo.getChannelKey());
		roomInfo.setSignalingKey(liveInfo.getSignalingKey());
		roomInfo.setAccount(liveInfo.getAccount());
		roomInfo.setStudentAccount(liveInfo.getStudentAccount());
		roomInfo.setTeacherAccount(liveInfo.getTeacherAccount());
		roomInfo.setRole(liveInfo.getRole());
		roomInfo.setCid(UuidUtils.getUuid(CourseModel.class, liveInfo.getCourseId()));
		roomInfo.setLid(UuidUtils.getUuid(LessonModel.class, liveInfo.getLessonId()));
		
		LiveRejoinInfoView rejoinInfo = new LiveRejoinInfoView(liveInfo.getCurrentFrameX(), 
				liveInfo.getCurrentFrameY());
		
		
		String framesInfoJson = liveInfo.getFramesInfo();
		
		LiveFrameInfoView framesInfoView = DoradoMapperUtils.fromJSON(framesInfoJson, LiveFrameInfoView.class);
		
		LiveInfoView liveInfoView = new LiveInfoView(roomInfo, framesInfoView, rejoinInfo);
		return liveInfoView;
	}
}
