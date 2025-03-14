package com.youshi.zebra.core;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.dorado.framework.utils.crypt.DES;
import com.youshi.zebra.audio.constants.AudioConstants;
import com.youshi.zebra.audio.model.AudioModel;
import com.youshi.zebra.book.constants.BookConstants;
import com.youshi.zebra.book.model.BookModel;
import com.youshi.zebra.book.model.ChapterModel;
import com.youshi.zebra.course.constants.CourseConstants;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.homework.constants.HomeworkConstants;
import com.youshi.zebra.homework.model.HomeworkAnswerModel;
import com.youshi.zebra.homework.model.HomeworkModel;
import com.youshi.zebra.image.constants.ImageConstants;
import com.youshi.zebra.image.model.ImageModel;
import com.youshi.zebra.lesson.constants.LessonConstants;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.media.model.MediaModel;
import com.youshi.zebra.order.model.OrderModel;
import com.youshi.zebra.reaction.model.StudentReactionModel;
import com.youshi.zebra.reaction.model.TeacherReactionModel;
import com.youshi.zebra.teacher.model.TeacherModel;
import com.youshi.zebra.user.constant.UserConstants;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.video.constants.VideoConstants;
import com.youshi.zebra.video.model.VideoModel;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年1月9日
 */
@Component
public class ZebraSystemInitBean {
	private Logger logger = LoggerFactory.getLogger(ZebraSystemInitBean.class);
	
	@PostConstruct
	public void init() {
		Map<Class<?>, DES> desMap = new HashMap<>();
		desMap.put(ImageModel.class, ImageConstants.IMAGE_DES);
		desMap.put(AudioModel.class, AudioConstants.AUDIO_DES);
		desMap.put(VideoModel.class, VideoConstants.VIDEO_DES);
		desMap.put(UserModel.class, UserConstants.USER_DES);
		desMap.put(TeacherModel.class, UserConstants.USER_DES);
		desMap.put(HomeworkModel.class, HomeworkConstants.HOMEWORK_DES);
		desMap.put(HomeworkAnswerModel.class, HomeworkConstants.HOMEWORK_ANSWER_DES);
		desMap.put(CourseModel.class, CourseConstants.COURSE_DES);
		desMap.put(CourseMetaModel.class, CourseConstants.COURSE_META_DES);
		desMap.put(LessonModel.class, LessonConstants.LESSON_DES);
		desMap.put(StudentReactionModel.class, LessonConstants.LESSON_DES);
		desMap.put(TeacherReactionModel.class, LessonConstants.LESSON_DES);
		
		
		desMap.put(BookModel.class, BookConstants.BOOK_DES);
		desMap.put(ChapterModel.class, BookConstants.BOOK_DES);
		
		desMap.put(OrderModel.class, BookConstants.BOOK_DES);
		desMap.put(MediaModel.class, BookConstants.BOOK_DES);
		
		UuidUtils.init(desMap);
		
		logger.info("Zebra api init OK");
	}
	
}
