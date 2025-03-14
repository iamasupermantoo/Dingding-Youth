package com.youshi.zebra.core.web.view;

import java.util.Collections;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.phantomthief.model.builder.impl.DefaultModelBuilderImpl;
import com.youshi.zebra.admin.adminuser.model.impl.AdminUserPassportModel;
import com.youshi.zebra.admin.log.model.AdminLogModel;
import com.youshi.zebra.audio.AudioService;
import com.youshi.zebra.audio.model.AudioModel;
import com.youshi.zebra.book.model.BookModel;
import com.youshi.zebra.book.model.FrameItem;
import com.youshi.zebra.book.service.BookService;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.course.model.CourseStatusModel;
import com.youshi.zebra.course.model.TryCourseModel;
import com.youshi.zebra.course.service.CourseMetaService;
import com.youshi.zebra.course.service.CourseService;
import com.youshi.zebra.course.service.CourseStatusService;
import com.youshi.zebra.homework.model.HomeworkModel;
import com.youshi.zebra.image.model.ImageModel;
import com.youshi.zebra.image.service.ImageService;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.lesson.service.LessonService;
import com.youshi.zebra.media.model.MediaModel;
import com.youshi.zebra.media.service.MediaService;
import com.youshi.zebra.order.model.OrderModel;
import com.youshi.zebra.reaction.model.StudentReactionModel;
import com.youshi.zebra.reaction.model.TeacherReactionModel;
import com.youshi.zebra.recommend.model.RecommendBannerModel;
import com.youshi.zebra.scholarship.model.ScholarshipRetainRecordModel;
import com.youshi.zebra.scholarship.service.ScholarshipService;
import com.youshi.zebra.teacher.model.TeacherModel;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.service.UserService;
import com.youshi.zebra.video.model.VideoModel;
import com.youshi.zebra.video.service.VideoService;

/**
 * 
 * Admin工程，实体builder
 * 
 * @author wangsch
 * @date 2016-09-12
 */
@Component("zebraAdminModelBuilder")
public class AdminModelBuilder extends DefaultModelBuilderImpl<ZebraBuildContext>{
    @Autowired
    private ImageService imageService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private MediaService mediaService;
    
    @Autowired
    private AudioService audioService;
    
    @Autowired
    private VideoService videoService;
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private LessonService lessonService;
    
    @Autowired
    private CourseStatusService courseStatusService;
    
    @Autowired
    private CourseMetaService courseMetaService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private ScholarshipService scholarshipService;
    
	@PostConstruct
	public void init() {
		// id extractors...
		this
		.addIdExtractor(UserModel.class, UserModel::getHeadImageId, ImageModel.class)
		.addIdExtractor(AdminUserPassportModel.class, AdminUserPassportModel::getId, UserModel.class)
		.addIdExtractor(AdminLogModel.class, AdminLogModel::getAdminId, UserModel.class)
		.addIdExtractor(TeacherModel.class, TeacherModel::getHeadImageId, ImageModel.class)
		.addIdExtractor(LessonModel.class, LessonModel::getTeacherId, UserModel.class)
		.addIdExtractor(LessonModel.class, LessonModel::getStudentId, UserModel.class)
		.addIdExtractor(HomeworkModel.class, HomeworkModel::getLessonId, LessonModel.class)
		.addIdExtractor(HomeworkModel.class, HomeworkModel::getCourseId, CourseModel.class)
		.addIdExtractor(CourseModel.class, CourseModel::getStudentId, UserModel.class)
		.addIdExtractor(CourseModel.class, CourseModel::getImageId, ImageModel.class)
		.addIdExtractor(CourseModel.class, CourseModel::getId, CourseStatusModel.class)
		.addIdExtractor(CourseMetaModel.class, CourseMetaModel::getImageId, ImageModel.class)
		.addIdExtractor(CourseMetaModel.class, CourseMetaModel::getBookId, BookModel.class)
		.addIdExtractor(TryCourseModel.class, TryCourseModel::getStudentId, UserModel.class)
		
		// media
		.addIdExtractor(FrameItem.class, FrameItem::getMediaId, MediaModel.class)
		.addIdExtractor(MediaModel.class, MediaModel::getImageId, ImageModel.class)
		.addIdExtractor(MediaModel.class, MediaModel::getAudioId, AudioModel.class)
		.addIdExtractor(MediaModel.class, MediaModel::getVideoId, VideoModel.class)
		.addIdExtractor(OrderModel.class, OrderModel::getUserId, UserModel.class)
		.addIdExtractor(OrderModel.class, OrderModel::getProductId, CourseMetaModel.class)
		
		// --banner
		.addIdExtractor(RecommendBannerModel.class, 
				RecommendBannerModel::getImageId, ImageModel.class)
		
		// 
		.addIdExtractor(ScholarshipRetainRecordModel.class, ScholarshipRetainRecordModel::getUserId, UserModel.class)
		
		// reaction
		.addIdExtractor(StudentReactionModel.class, StudentReactionModel::getCourseId, CourseModel.class)
		.addIdExtractor(StudentReactionModel.class, StudentReactionModel::getTeacherId, UserModel.class)
		.addIdExtractor(StudentReactionModel.class, StudentReactionModel::getStudentId, UserModel.class)
		.addIdExtractor(TeacherReactionModel.class, TeacherReactionModel::getCourseId, CourseModel.class)
		.addIdExtractor(TeacherReactionModel.class, TeacherReactionModel::getTeacherId, UserModel.class)
		.addIdExtractor(TeacherReactionModel.class, TeacherReactionModel::getStudentId, UserModel.class)
		
        // value extractors...	// QUES 干掉这个会出错不
       .addValueExtractor(ImageModel.class,
                model -> Collections.singletonMap(model.getId(), model), ImageModel.class)
				
        // data builders...
        .addDataBuilder(ImageModel.class, imageService::getByIds)
        .addDataBuilder(AudioModel.class, audioService::getByIds)
        .addDataBuilder(LessonModel.class, lessonService::getByIds)
        .addDataBuilder(VideoModel.class, videoService::getByIds)
        .addDataBuilder(UserModel.class, userService::getByIds)
        .addDataBuilder(MediaModel.class, mediaService::getByIds)
        .addDataBuilder(CourseModel.class, courseService::getByIds)
        .addDataBuilder(BookModel.class, bookService::getByIds)
        .addDataBuilder(CourseMetaModel.class, courseMetaService::getByIds)
        .addDataBuilder(CourseStatusModel.class, courseStatusService::getByIds)
		;
        
        // data builders Ex...
	}
}
