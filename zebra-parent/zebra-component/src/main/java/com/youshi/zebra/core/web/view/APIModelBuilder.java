package com.youshi.zebra.core.web.view;

import java.util.Collections;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.phantomthief.model.builder.impl.DefaultModelBuilderImpl;
import com.youshi.zebra.admin.adminuser.model.impl.AdminUserPassportModel;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.course.model.LiveMetaModel;
import com.youshi.zebra.course.service.CourseMetaService;
import com.youshi.zebra.course.service.CourseService;
import com.youshi.zebra.homework.model.HomeworkModel;
import com.youshi.zebra.image.model.ImageModel;
import com.youshi.zebra.image.service.ImageService;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.lesson.service.LessonService;
import com.youshi.zebra.order.model.OrderModel;
import com.youshi.zebra.reaction.model.StudentReactionModel;
import com.youshi.zebra.reaction.model.TeacherReactionModel;
import com.youshi.zebra.recommend.model.RecommendBannerModel;
import com.youshi.zebra.student.model.TeacherStudentModel;
import com.youshi.zebra.teacher.model.TeacherModel;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.service.UserService;

/**
 * 
 * API，实体builder
 * 
 * @author wangsch
 * @date 2016-09-12
 */
@Component("zebraApiModelBuilder")
public class APIModelBuilder extends DefaultModelBuilderImpl<ZebraBuildContext>{
    @Autowired
    private ImageService imageService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private CourseMetaService courseMetaService;
    
    @Autowired
    private LessonService lessonService;
    
    @Autowired
    private UserService UserService;
    
	@PostConstruct
	public void init() {
		// id extractors...
		this
		.addIdExtractor(UserModel.class, UserModel::getHeadImageId, ImageModel.class)
		.addIdExtractor(AdminUserPassportModel.class, AdminUserPassportModel::getId, UserModel.class)
		.addIdExtractor(TeacherModel.class, TeacherModel::getHeadImageId, ImageModel.class)
		.addIdExtractor(CourseModel.class, CourseModel::getImageId, ImageModel.class)
		.addIdExtractor(CourseModel.class, CourseModel::getCmId, CourseMetaModel.class)
		.addIdExtractor(LessonModel.class, LessonModel::getCourseId, CourseModel.class)
		.addIdExtractor(LessonModel.class, LessonModel::getStudentId, UserModel.class)
		.addIdExtractor(LessonModel.class, LessonModel::getTeacherId, UserModel.class)
		.addIdExtractor(CourseMetaModel.class, CourseMetaModel::getImageId, ImageModel.class)
		.addIdExtractor(CourseMetaModel.class, CourseMetaModel::getBigImageId, ImageModel.class)
		.addIdExtractor(StudentReactionModel.class, StudentReactionModel::getCourseId, CourseModel.class)
		.addIdExtractor(StudentReactionModel.class, StudentReactionModel::getTeacherId, UserModel.class)
		.addIdExtractor(StudentReactionModel.class, StudentReactionModel::getStudentId, UserModel.class)
		// teacher
		.addIdExtractor(TeacherReactionModel.class, TeacherReactionModel::getCourseId, CourseModel.class)
		.addIdExtractor(TeacherReactionModel.class, TeacherReactionModel::getTeacherId, UserModel.class)
		.addIdExtractor(TeacherReactionModel.class, TeacherReactionModel::getStudentId, UserModel.class)
		
		// live meta
		.addIdExtractor(LiveMetaModel.class, LiveMetaModel::getImageId, ImageModel.class)
		
		
		// --banner
		.addIdExtractor(RecommendBannerModel.class, 
				RecommendBannerModel::getImageId, ImageModel.class)
		
		// homework
		.addIdExtractor(HomeworkModel.class, HomeworkModel::getLessonId, LessonModel.class)
		.addIdExtractor(HomeworkModel.class, HomeworkModel::getStudentId, UserModel.class)
		.addIdExtractor(HomeworkModel.class, HomeworkModel::getTeacherId, UserModel.class)
		.addIdExtractor(OrderModel.class, OrderModel::getProductId, CourseMetaModel.class)
		
        // value extractors...	// QUES 干掉这个会出错不
       .addValueExtractor(ImageModel.class,
                model -> Collections.singletonMap(model.getId(), model), ImageModel.class)
				
        // data builders...
        .addDataBuilder(ImageModel.class, imageService::getByIds)
        .addDataBuilder(LessonModel.class, lessonService::getByIds)
        .addDataBuilder(CourseModel.class, courseService::getByIds)
        .addDataBuilder(UserModel.class, UserService::getByIds)
        .addDataBuilder(CourseMetaModel.class, courseMetaService::getByIds)
		;
        
        // data builders Ex...
	}
}
