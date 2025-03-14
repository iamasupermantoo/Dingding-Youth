package com.youshi.zebra.live.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.dorado.mvc.reqcontext.Platform;
import com.google.common.collect.ImmutableMap;
import com.youshi.zebra.aliyun.constants.AliyunConstants;
import com.youshi.zebra.audio.AudioService;
import com.youshi.zebra.audio.model.AudioModel;
import com.youshi.zebra.audio.utils.AudioUtils;
import com.youshi.zebra.book.constants.FrameConfigConstants;
import com.youshi.zebra.book.model.FrameItem;
import com.youshi.zebra.book.model.ImageItem;
import com.youshi.zebra.book.model.ParsedFrameItem;
import com.youshi.zebra.book.service.FrameConfigService;
import com.youshi.zebra.core.exception.EntityNotFoundException;
import com.youshi.zebra.core.utils.RedisDAOUtils;
import com.youshi.zebra.counts.service.UserCountsService;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.course.model.LiveMetaModel;
import com.youshi.zebra.course.service.CourseStatusService;
import com.youshi.zebra.course.service.LiveMetaService;
import com.youshi.zebra.exception.common.EntityNotNormalException;
import com.youshi.zebra.exception.common.EntityStatusException;
import com.youshi.zebra.exception.common.ForbiddenException;
import com.youshi.zebra.homework.service.HomeworkService;
import com.youshi.zebra.image.constants.AliyunOssConstants;
import com.youshi.zebra.image.model.ImageModel;
import com.youshi.zebra.image.service.ImageService;
import com.youshi.zebra.lesson.constants.LessonStatus;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.lesson.model.LessonModel.LessonKeys;
import com.youshi.zebra.lesson.service.LessonService;
import com.youshi.zebra.live.constants.AgoraConstants;
import com.youshi.zebra.live.constants.AgoraLiveRole;
import com.youshi.zebra.live.constants.RoomInfoField;
import com.youshi.zebra.live.constants.StudentOnlineStatus;
import com.youshi.zebra.live.constants.TeacherOnlineStatus;
import com.youshi.zebra.live.dao.LiveRoomInfoRedisDAO;
import com.youshi.zebra.live.exception.LiveLessonStatusInvalidException;
import com.youshi.zebra.live.model.LiveInfo;
import com.youshi.zebra.live.model.OssSdkInfo;
import com.youshi.zebra.live.model.RoomInfoModel;
import com.youshi.zebra.live.utils.AgoraUtils;
import com.youshi.zebra.media.constants.MediaType;
import com.youshi.zebra.media.model.MediaModel;
import com.youshi.zebra.media.service.MediaService;
import com.youshi.zebra.reaction.service.StudentReactionService;
import com.youshi.zebra.reaction.service.TeacherReactionService;
import com.youshi.zebra.video.model.VideoModel;
import com.youshi.zebra.video.service.VideoService;
import com.youshi.zebra.video.utils.VideoUtils;

/**
 * 
 * 
 * 直播课堂，Service实现。
 * 
 * 特别说明的是，学生和家长使用同一个账号（同一个用户id），但是客户端（{@link Platform}）不同，家长使用：ios或android，学生使用：ipad。
 * 有些方法中，针对{@link Platform}有判断逻辑。
 * 
 * @author wangsch
 * @date 2017年3月14日
 */
@Service
public class LiveService {
	private static final Logger logger = LoggerFactory.getLogger(LiveService.class);

	private static final int LESSON_DURATION = 25;
	
	@Autowired
	private LiveRoomInfoRedisDAO roomInfoDAO;
	
	@Autowired
	private FrameConfigService frameConfigService;
	
	@Autowired
	private MediaService mediaService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private AudioService audioService;
	
	@Autowired
	private VideoService videoService;
	
	@Autowired
	private LessonService lessonService;
	
	@Autowired
	private CourseStatusService courseStatusService;
	
	@Autowired
	private UserCountsService userCountsService;
	
	@Autowired
	private HomeworkService homeworkService;
	
	@Autowired
	private TeacherReactionService teacherReactionService;
	
	@Autowired
	private LiveMetaService liveMetaService;
	
	
	@Autowired
	private StudentReactionService studentReactionService;
	
	public RoomInfoModel getRoomInfo(Integer courseId, Integer lessonId) {
		String channel = channelName(courseId, lessonId);
		return roomInfoDAO.getRoomInfo(channel);
	}
	
	public LiveInfo getStudentOpenCourseLiveInfo(Integer studentId, Integer lmId) {
		String channel = String.valueOf(lmId);
		long account = studentId;
		
		LiveMetaModel lm = liveMetaService.getById(lmId);
		String channelKey = AgoraUtils.genChannelKey(channel, account);
		String signalingKey = AgoraUtils.genSignalingKey(account);
		
		LiveInfo result = new LiveInfo()
				.setChannelName(channel)
				.setAccount(account)
				.setTeacherAccount(lm.getTeacherId())
				.setChannelKey(channelKey)
				.setSignalingKey(signalingKey)
				.setRole(AgoraLiveRole.Audience.getValue())
				;
		
		return result;
	}
	
	public LiveInfo getTeacherOpenCourseLiveInfo(Integer teacherId, Integer lmId) {
		String channel = String.valueOf(lmId);
		long account = teacherId;
		
		LiveMetaModel lm = liveMetaService.getById(lmId);
		String channelKey = AgoraUtils.genChannelKey(channel, account);
		String signalingKey = AgoraUtils.genSignalingKey(account);
		
		LiveInfo result = new LiveInfo()
				.setChannelName(channel)
				.setAccount(account)
				.setTeacherAccount(lm.getTeacherId())
				.setChannelKey(channelKey)
				.setSignalingKey(signalingKey)
				.setRole(AgoraLiveRole.Broadcaster.getValue())
				;
		
		return result;
	}
	
	
	/**
	 * 学生，获取直播信息
	 * @param platform 
	 * 
	 */
	public LiveInfo getStudentLiveInfo(Integer studentId, Integer courseId, Integer lessonId, 
			Platform platform) {
		LessonModel lesson = checkLessonStatus(courseId, lessonId);
		checkStudentId(studentId, courseId, lessonId, lesson);
		// TODO 时间段检测
		
		Integer chapterId = lesson.getChapterId();
		String channel = channelName(courseId, lessonId);
		
		RoomInfoModel roomInfo = roomInfoDAO.getRoomInfo(channel);
		
		String framesInfoJson = initFramesInfoJson(channel, courseId, lessonId, chapterId, roomInfo);
		
		Integer role = null;
		long account = 0;
		if(platform == Platform.IPAD) {
			role = AgoraLiveRole.Broadcaster.getValue();
			account = lesson.getStudentId();
		} else {
			role = AgoraLiveRole.Audience.getValue();
			account = 0;
		}
		
		String channelKey = AgoraUtils.genChannelKey(channel, account);
		String signalingKey = AgoraUtils.genSignalingKey(account);
		LiveInfo result = new LiveInfo()
				.setChannelName(channel)
				.setAccount(account)
				.setStudentAccount(lesson.getStudentId())
				.setTeacherAccount(lesson.getTeacherId())
				.setChannelKey(channelKey)
				.setSignalingKey(signalingKey)
				.setRole(role)
				.setFramesInfo(framesInfoJson)
				.setCurrentFrameX(roomInfo.getCurrentFrameX())
				.setCurrentFrameY(roomInfo.getCurrentFrameY())
				.setCourseId(courseId)
				.setLessonId(lessonId)
				;
		
		String field = RoomInfoField.STUDENT_ONLINE_STATUS;
		roomInfoDAO.setField(channel, field, 
				String.valueOf(StudentOnlineStatus.STUDENT_ONLINE.getValue()));
		return result;
	}
	
	/**
	 * 观众加入房间
	 * 
	 * @param userId
	 * @param courseId
	 * @param lessonId
	 */
	public void audienceJoin(Integer userId, Integer courseId, Integer lessonId) {
		LessonModel lesson = checkLessonStatus(courseId, lessonId);
		checkStudentId(userId, courseId, lessonId, lesson);
		
		// TODO 时间
		String channel = channelName(courseId, lessonId);
		RoomInfoModel roomInfo = roomInfoDAO.getRoomInfo(channel);
		
		List<Integer> audienceIdList = roomInfo.getAudienceIdList();
		if(audienceIdList==null) {
			audienceIdList = new ArrayList<>();
		}
		
		if(audienceIdList.contains(userId)) {
			logger.error("You(audience) have already joined. courseId: {}, lessonId: {}, userId: {}", 
					courseId, lessonId, userId);
			throw new LiveLessonStatusInvalidException();
		}
		
		audienceIdList.add(userId);
		roomInfoDAO.setField(channel, RoomInfoField.AUDIENCE_LIST, DoradoMapperUtils.toJSON(audienceIdList));
		
		logger.info("Audience join live succ. userId: {}, courseId: {}, lessonId: {}", 
				userId, courseId, lessonId);
	}
	
	/**
	 * 学生加入房间
	 * 
	 * @param studentId		学生id
	 * @param courseId		课程id
	 * @param lessonId		lesson id
	 */
	public void studentJoin(Integer studentId, Integer courseId, Integer lessonId, Platform platform) {
		// 不是ipad，则认为是家长通过手机端，作为观众加入房间
		if(platform != Platform.IPAD) {
			audienceJoin(studentId, courseId, lessonId);
			return;
		}
		
		// 学生通过ipad，加入直播课堂
		LessonModel lesson = checkLessonStatus(courseId, lessonId);
		checkStudentId(studentId, courseId, lessonId, lesson);
		
		String channel = channelName(courseId, lessonId);
		RoomInfoModel roomInfo = roomInfoDAO.getRoomInfo(channel);
		
		if(roomInfo.getStudentOnlineStatus() != null) {
			logger.error("You have already joined. courseId: {}, lessonId: {}, studentId: {}", 
					courseId, lessonId, studentId);
			throw new LiveLessonStatusInvalidException();
		}
		
		roomInfoDAO.setField(channel, RoomInfoField.STUDENT_ONLINE_STATUS, 
				String.valueOf(StudentOnlineStatus.STUDENT_ONLINE.getValue()));
		
		logger.info("Student join live succ. studentId: {}, courseId: {}, lessonId: {}", 
				studentId, courseId, lessonId);
	}
	
	public void studentLeave(Integer userId, Integer courseId, Integer lessonId) {
		checkLessonStatus(courseId, lessonId);
		
		String channel = channelName(courseId, lessonId);
		String field = RoomInfoField.STUDENT_ONLINE_STATUS;
		roomInfoDAO.setField(channel, field, 
				String.valueOf(StudentOnlineStatus.STUDENT_OFFLINE.getValue()));
		
		logger.info("Teacher leave live. teacherId: {}, courseId: {}, lessonId: {}", 
				userId, courseId, lessonId);
	}
	
	// ----------------------------------------- 老师 -----------------------------------------
	/**
	 * 老师，获取直播信息
	 */
	public LiveInfo getTeacherLiveInfo(Integer teacherId, Integer courseId, Integer lessonId) {
		LessonModel lesson = checkLessonStatus(courseId, lessonId);
		checkTeacherId(teacherId, courseId, lessonId, lesson);
		
		// TODO 时间段检测
		
		
		Integer chapterId = lesson.getChapterId();
		String channel = channelName(courseId, lessonId);
		
		RoomInfoModel roomInfo = roomInfoDAO.getRoomInfo(channel);
		
		String framesInfoJson = initFramesInfoJson(channel, courseId, lessonId, chapterId, roomInfo);
		
		Integer account = lesson.getTeacherId();
		String channelKey = AgoraUtils.genChannelKey(channel, account);
		String signalingKey = AgoraUtils.genSignalingKey(account);
		
		LiveInfo result = new LiveInfo()
				.setAppId(AgoraConstants.APP_ID)
				.setChannelName(channel)
				.setAccount(lesson.getTeacherId())
				.setStudentAccount(lesson.getStudentId())
				.setTeacherAccount(lesson.getTeacherId())
				.setChannelKey(channelKey)
				.setSignalingKey(signalingKey)
				.setRole(AgoraLiveRole.Broadcaster.getValue())
				.setFramesInfo(framesInfoJson)
				.setCurrentFrameX(roomInfo.getCurrentFrameX())
				.setCurrentFrameY(roomInfo.getCurrentFrameY())
				.setCourseId(courseId)
				.setLessonId(lessonId)
				;
		
		String field = RoomInfoField.TEACHER_ONLINE_STATUS;
		roomInfoDAO.setField(channel, field, 
				String.valueOf(TeacherOnlineStatus.TEACHER_ONLINE.getValue()));
		
		return result;
	}
	
	public void prepare(Integer teacherId, Integer courseId, Integer lessonId) {
		LessonModel lesson = checkLessonStatus(courseId, lessonId);
		checkTeacherId(teacherId, courseId, lessonId, lesson);
		
		// TODO 开课前10分钟 - 结束课程时间
		String channel = channelName(courseId, lessonId);
		String field = RoomInfoField.TEACHER_ONLINE_STATUS;
		
		// FIXME
		// roomInfoDAO.checkEmpty(channel, field);
		roomInfoDAO.setField(channel, field, 
				String.valueOf(TeacherOnlineStatus.TEACHER_READY.getValue()));
		
		logger.info("Teacher prepare succ. teacherId: {}, courseId: {}, lessonId: {}", 
				teacherId, courseId, lessonId);
	}
	
	public void teacherLeave(Integer userId, Integer courseId, Integer lessonId) {
		checkLessonStatus(courseId, lessonId);
		
		String channel = channelName(courseId, lessonId);
		String field = RoomInfoField.TEACHER_ONLINE_STATUS;
		roomInfoDAO.setField(channel, field, 
				String.valueOf(TeacherOnlineStatus.TEACHER_OFFLINE.getValue()));
		
		logger.info("Teacher leave live. teacherId: {}, courseId: {}, lessonId: {}", 
				userId, courseId, lessonId);
	}
	
	public void start(Integer teacherId, Integer courseId, Integer lessonId) {
		LessonModel lesson = checkLessonStatus(courseId, lessonId);
		checkTeacherId(teacherId, courseId, lessonId, lesson);
		
		// TODO 开始时间 - 结束课程时间+10分钟
		
		String channel = channelName(courseId, lessonId);
		String field = RoomInfoField.TEACHER_ONLINE_STATUS;
		// FIXME
//		roomInfoDAO.checkField(channel, field, 
//				String.valueOf(TeacherOnlineStatus.TEACHER_ONLINE_READY.getValue()));
		
		roomInfoDAO.setField(channel, field, 
				String.valueOf(TeacherOnlineStatus.TEACHER_ON_PROGRESS.getValue()));
		
		lessonService.updateStatus(teacherId, courseId, lessonId, LessonStatus.ONPROCESS);
		logger.info("Teacher start live succ. teacherId: {}, courseId: {}, lessonId: {}", 
				teacherId, courseId, lessonId);
	}
	
	public void stop(Integer teacherId, Integer courseId, Integer lessonId) {
		LessonModel lesson = checkLessonStatus(courseId, lessonId);
		checkTeacherId(teacherId, courseId, lessonId, lesson);
		// TODO 结束课程时间 - 无穷
		
		String channel = channelName(courseId, lessonId);
		String field = RoomInfoField.TEACHER_ONLINE_STATUS;
		// FIXME
//		roomInfoDAO.checkField(channel, field, 
//				String.valueOf(TeacherOnlineStatus.TEACHER_ONLINE_ON_PROGRESS.getValue()));
		
		// 更新lesson状态哦，亲
		lessonService.updateStatus(teacherId, courseId, lessonId, LessonStatus.FINISHED);
		courseStatusService.incrFinishedCnt(courseId);
		
		// 生成作业和反馈记录哦，亲
		homeworkService.createHomework(courseId, lessonId);
		teacherReactionService.createReaction(teacherId, courseId, lessonId);
		studentReactionService.createReaction(teacherId, courseId, lessonId);
		
		// 计数哦，亲
		int userId = lesson.getStudentId();
		userCountsService.addFinishedCount(userId, 1);
		userCountsService.addDuration(userId, LESSON_DURATION);
		
		Long ret = roomInfoDAO.expire(channel);
		RedisDAOUtils.checkRet_1(ret);
		
		logger.info("Teacher stop live succ. teacherId: {}, courseId: {}, lessonId: {}", 
				teacherId, courseId, lessonId);
	}
	
	public void uploadOK(Integer teacherId, Integer courseId, Integer lessonId) {
		LessonModel lesson = lessonService.getOneLesson(courseId, lessonId);
		if(lesson == null) {
			throw new EntityNotFoundException();
		}
		
		Map<String, Object> dataMap = lesson.resolvedData();
		dataMap.put(LessonKeys.video_uploaded.name(), true);
		lessonService.updateData(courseId, lessonId, DoradoMapperUtils.toJSON(dataMap));
		
		logger.info("Teacher upload video succ. teacherId: {}, courseId: {}, lessonId: {}", 
				teacherId, courseId, lessonId);
	}
	
	public void changeframe(Integer teacherId, Integer courseId, Integer lessonId, 
			Integer frameX, Integer frameY) {
		LessonModel lesson = checkLessonStatus(courseId, lessonId);
		checkTeacherId(teacherId, courseId, lessonId, lesson);
		// TODO 时间
		
		String channel = channelName(courseId, lessonId);
//		roomInfoDAO.checkField(channel, RoomInfoField.TEACHER_ONLINE_STATUS, 
//				String.valueOf(TeacherOnlineStatus.TEACHER_ONLINE_ON_PROGRESS.getValue()));
		
		String ret = roomInfoDAO.setFields(channel, 
				ImmutableMap.<String, String>builder()
				.put(RoomInfoField.CURRENT_FRAME_X, String.valueOf(frameX))
				.put(RoomInfoField.CURRENT_FRAME_Y, String.valueOf(frameY))
				.build());
		
		RedisDAOUtils.checkOK(ret);
	}
	
	// ----------------------------------------- private methods -----------------------------------------


	/**
	 * 检查lesson是否允许直播相关操作，要求当前lesson非“关闭”状态
	 * 
	 * @param courseId
	 * @param lessonId
	 * @return
	 */
	private LessonModel checkLessonStatus(Integer courseId, Integer lessonId) 
			throws EntityNotNormalException, EntityStatusException {
		LessonModel lesson = lessonService.getOneLesson(courseId, lessonId);
		if(lesson == null) {
			throw new EntityNotNormalException();
		}
		
		return lesson;
	}

	/**
	 * @param teacherId
	 * @param courseId
	 * @param lessonId
	 * @param lesson
	 */
	private void checkTeacherId(Integer teacherId, Integer courseId, Integer lessonId, LessonModel lesson) {
		if(lesson.getTeacherId() != teacherId ) {
			logger.error("This is NOT YOUR lesson. teacherId: {}, courseId: {}, lessonId: {}",
					teacherId, courseId, lessonId);
			throw new ForbiddenException();
		}
	}

	/**
	 * @param studentId
	 * @param courseId
	 * @param lessonId
	 * @param lesson
	 */
	private void checkStudentId(Integer studentId, Integer courseId, Integer lessonId, LessonModel lesson) {
		if(lesson.getStudentId() != studentId ) {
			logger.error("This is NOT YOUR lesson. studentId: {}, courseId: {}, lessonId: {}",
					studentId, courseId, lessonId);
			throw new ForbiddenException();
		}
	}
	
	private static String channelName(Integer courseId, Integer lessonId) {
		return DigestUtils.md5Hex(courseId + "@" + lessonId);
	}

	private String initFramesInfoJson(String channel, Integer courseId, Integer lessonId, Integer chapterId, 
			RoomInfoModel roomInfo) {
		String framesInfoJson = roomInfo.getFramesInfoJson();
		if(StringUtils.isEmpty(framesInfoJson)) {
			List<FrameItem> frames = frameConfigService.getFrames(chapterId, false);
			framesInfoJson = parseFramesJson(frames);
			
			roomInfoDAO.setField(channel, RoomInfoField.FRAMES_INFO_JSON, framesInfoJson);
			
			logger.info("Frames info inited succ. courseId: {}, lessonId: {}", 
					courseId, lessonId);
			
		}
		return framesInfoJson;
	}
	
	private String parseFramesJson(List<FrameItem> frames) {
		// 查询MediaMode
		Set<Integer> mediaIds = new HashSet<>();
		for(FrameItem frame : frames) {
			mediaIds.add(frame.getMediaId());
			List<FrameItem> children = frame.getChildren();
			if(children != null && children.size() != 0) {
				for(FrameItem child : children) {
					mediaIds.add(child.getMediaId());
				}
			}
		}
		
		Map<Integer, MediaModel> medias = mediaService.getByIds(mediaIds);
		
		// 查询各种媒体类型Model
		Set<Integer> imageIds = new HashSet<>();
		Set<Integer> audioIds = new HashSet<>();
		Set<Integer> videoIds = new HashSet<>();
		for(Entry<Integer, MediaModel> entry : medias.entrySet()) {
			MediaModel media = entry.getValue();
			MediaType type = MediaType.fromValue(media.getType());
			switch (type) {
			case Image:
				imageIds.add(media.getImageId());
				break;
			case Audio:
				audioIds.add(media.getAudioId());
				break;
			case Video:
				videoIds.add(media.getVideoId());
				break;

			default:
				throw new UnsupportedOperationException();
			}
		}
		Map<Integer, ImageModel> images = imageService.getByIds(imageIds);
		Map<Integer, AudioModel> audios = audioService.getByIds(audioIds);
		Map<Integer, VideoModel> videos = videoService.getByIds(videoIds);
		
		// 将原始的FrameItem转换为ParsedFrameItem
		List<ParsedFrameItem> parsedFrameItems = doParseFrames(frames, medias, images, audios, videos);
		
		String version = FrameConfigConstants.VERSION;
		ImmutableMap<String, Object> framesInfoMap = ImmutableMap.<String, Object>builder()
				.put("version", version)
				.put("frames", parsedFrameItems)
			.build();
		
		return DoradoMapperUtils.toJSON(framesInfoMap);
	}
	
	List<ParsedFrameItem> doParseFrames(List<FrameItem> frames, Map<Integer, MediaModel> medias,
			Map<Integer, ImageModel> images, Map<Integer, AudioModel> audios, Map<Integer, VideoModel> videos) {
		List<ParsedFrameItem> result = new ArrayList<>();
		for(int idx=0; idx<frames.size(); idx++) {
			FrameItem frame = frames.get(idx);
			ParsedFrameItem parsedFrame = parseOne(frame, idx, medias.get(frame.getMediaId()), images, audios, videos);
			
			if(frame.getChildren() != null && frame.getChildren().size() != 0) {
				List<ParsedFrameItem> children = doParseFrames(frame.getChildren(), medias, images, audios, videos);
				parsedFrame.setChildren(children);
			}
			result.add(parsedFrame);
		}
		return result;
	}
	
	private ParsedFrameItem parseOne(FrameItem frame, Integer idx, MediaModel media,
			Map<Integer, ImageModel> images, Map<Integer, AudioModel> audios, Map<Integer, VideoModel> videos) {
		ParsedFrameItem parsedFrame = new ParsedFrameItem();
		parsedFrame.setIdx(idx);
		parsedFrame.setType(frame.getType());
		MediaType type = MediaType.fromValue(frame.getType());
		switch(type) {
		case Image:
			
			Integer imageId = media.getImageId();
			ImageModel image = images.get(imageId);
			String imageUuid = UuidUtils.getUuid(ImageModel.class, imageId);
			parsedFrame.setImage(new ImageItem(imageUuid, image.getWidth(), image.getHeight(), image.getFormat()));
			break;
		case Audio:
			AudioModel audio = audios.get(media.getAudioId());
			parsedFrame.setAudio(AudioUtils.getUrl(audio));
			
			break;
		case Video:
			VideoModel video = videos.get(media.getVideoId());
			parsedFrame.setVideo(VideoUtils.getUrl(video));
			
			break;
		default:
			throw new UnsupportedOperationException("Unsupported media type: " + type);
		}
		return parsedFrame;
	}

	
	
	/**
	 * @param teacherId
	 * @return
	 */
	// TODO 频率限制和缓存
	public OssSdkInfo getOssSdkInfo(Integer teacherId, Integer courseId, Integer lessonId) {
		String key = videoOssKey(courseId, lessonId);
		
		OssSdkInfo result = new OssSdkInfo();
		result.setKey(key);
		// FIXME 获取sts 临时keyid、keysecret，而不是写死
		result.setAccessKeyId(AliyunConstants.ACCESS_KEY_ID);
		result.setAccessKeySecret(AliyunConstants.ACCESS_KEY_SECRET);
		result.setBucketName(AliyunOssConstants.VIDEO_BUCKET_NAME);
		result.setEndpoint(AliyunOssConstants.OSS_ENDPOINT);
		
////		String ramPolicy = aliyunService.getRamPolicy("Custom", "AliyunOSSZebraVideoDevUpload");
//		String ramPolicy = null;
//		TwoTuple<String, String> stsAccount = aliyunService.getStsAccount("acs:ram::1887933298995758:role/aliyunosszebravideodevuploadrole", 
//				roleSessionName(teacherId), ramPolicy, 
//				TimeUnit.HOURS.toSeconds(1));
//		result.setAccessKeyId(stsAccount.first);
//		result.setAccessKeySecret(stsAccount.second);
		
		return result;
	}
	
	private String roleSessionName(Integer teacherId) {
		return "role-" + teacherId;
	}

	public static String videoOssKey(Integer courseId, Integer lessonId) {
		return UuidUtils.getUuid(CourseModel.class, courseId) 
				+ "_" + UuidUtils.getUuid(LessonModel.class, lessonId) + ".mp4";
	}
	
	public static void main(String[] args) {
		System.out.println(channelName(41, 66));
	}
	
}
