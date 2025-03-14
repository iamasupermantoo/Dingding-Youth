package com.youshi.zebra.core.web.view;

import java.util.Map;

import com.github.phantomthief.model.builder.context.impl.DefaultBuildContextImpl;
import com.youshi.zebra.audio.model.AudioModel;
import com.youshi.zebra.book.model.BookModel;
import com.youshi.zebra.counts.model.UserCountsModel;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.course.model.CourseStatusModel;
import com.youshi.zebra.image.model.ImageModel;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.media.model.MediaModel;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.video.model.VideoModel;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public class ZebraBuildContext extends DefaultBuildContextImpl {
    private int visitor;

    public ZebraBuildContext() {
    }

    public int getVisitor() {
        return visitor;
    }

    public void setVisitor(int visitor) {
        this.visitor = visitor;
    }
    
    /*
     * getXxx(key, type), 获取当前context下指定类型的对象
     */
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ImageModel getImage(Integer imageId) {
    	return getImageMap().get(imageId);
    }
    
    public LessonModel getLesson(Integer lessonId) {
    	return getLessonMap().get(lessonId);
    }

	public UserModel getUser(int userId) {
		return getUserMap().get(userId);
	}
	
	public CourseMetaModel getCourseMeta(int cmId) {
		return getCourseMetaMap().get(cmId);
	}
	
	public CourseModel getCourse(int courseId) {
		return getCourseMap().get(courseId);
	}
	
	public MediaModel getMedia(int mediaId) {
		return getMediaMap().get(mediaId);
	}
	
	public AudioModel getAudio(int audioId) {
		return getAudioMap().get(audioId);
	}
	
	public VideoModel getVideo(int videoId) {
		return getVideoMap().get(videoId);
	}
	
	public BookModel getBook(int bookId) {
		return getBookMap().get(bookId);
	}
	
	public CourseStatusModel getCourseStatus(int courseId) {
		return getCourseStatusMap().get(courseId);
	}
    
    
	/*
     * getXxxMap(), 获取当前context下指定类型的对象集和，比如：getImageMap获取Image对象集合
     */
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Map<Integer, ImageModel> getImageMap() {
        return getData(ImageModel.class);
    }

	private Map<Integer, LessonModel> getLessonMap() {
		return getData(LessonModel.class);
	}
    
    public Map<Integer, UserModel> getUserMap() {
        return getData(UserModel.class);
    }
    
    public Map<Integer, CourseMetaModel> getCourseMetaMap() {
    	return getData(CourseMetaModel.class);
    }
    
    public Map<Integer, CourseModel> getCourseMap() {
        return getData(CourseModel.class);
    }
    
    public Map<Integer, MediaModel> getMediaMap() {
        return getData(MediaModel.class);
    }
    
    public Map<Integer, AudioModel> getAudioMap() {
        return getData(AudioModel.class);
    }
    
    public Map<Integer, VideoModel> getVideoMap() {
        return getData(VideoModel.class);
    }
    
    public Map<Integer, BookModel> getBookMap() {
        return getData(BookModel.class);
    }
    
    public Map<Integer, CourseStatusModel> getCourseStatusMap() {
    	return getData(CourseStatusModel.class);
    }
}
