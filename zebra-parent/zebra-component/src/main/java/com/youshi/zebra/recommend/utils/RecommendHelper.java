package com.youshi.zebra.recommend.utils;

import org.springframework.stereotype.Component;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.model.LiveMetaModel;
import com.youshi.zebra.recommend.constants.RecommendFeedType;

/**
 * 首页推荐，helper
 * 
 * @author wangsch
 * @date 2016-09-26
 */
@Component
public class RecommendHelper {
	
	/**
	 * 
	 * @param dataId
	 * @param type
	 * @param cls
	 * @return
	 */
//	@SuppressWarnings("unchecked")
//	public static <T> T dataModel(Integer dataId, RecommendFeedType type, Class<T> cls) {
//		Object model = null;
//		switch (type) {
//		case article:
//			model = postService.getById(dataId);
//			break;
//		case picture:
//			model = postService.getById(dataId);
//			break;
//		case pictures:
//			model = postService.getById(dataId);
//			break;
//		default:
//			throw new IllegalArgumentException();
//		}
//		boolean ok = model != null && model.getClass().getName().equals(cls.getName());
//		return ok? (T)model : null;
//	}
	
	/**
	 * 根据{@link RecommendFeedType}，获取feed的uuid
	 * @param type	{@link RecommendFeedType}
	 * @param id		数字id
	 * @return			uuid
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final String getFeedUuid(RecommendFeedType type, Number id) {
		Class clazz = null;
		switch (type) {
		case TRY_COURSE: 
			clazz = CourseMetaModel.class;
			break;
		case OPEN_COURSE:
			clazz = LiveMetaModel.class;
			break;
		default:
			break;
		}
		
		String uuid = UuidUtils.getUuid(clazz, id);
		return uuid;
	}
	
	/**
	 * 返回推荐feed的作者用户id
	 * 
	 * @param dataId
	 * @param type
	 * @return				可能会返回null，获取失败或者根本没有作者id
	 */
//	public static final Integer getFeedAuthorId(Integer dataId, RecommendFeedType type) {
//		Integer authorId = null;
//		PostModel postModel = null;
//		switch (type) {
//		case article: 
//			postModel = dataModel(dataId, type, PostModel.class);
//			if(postModel != null) {
//				authorId = postModel.getAuthorId();
//			}
//			break;
//		case picture:
//			postModel = dataModel(dataId, type, PostModel.class);
//			if(postModel != null) {
//				authorId = postModel.getAuthorId();
//			}
//			break;
//		case pictures:
//			postModel = dataModel(dataId, type, PostModel.class);
//			if(postModel != null) {
//				authorId = postModel.getAuthorId();
//			}
//			break;
//		default:
//			throw new IllegalArgumentException("Unknow type: " + type);
//		}
//		
//		return authorId;
//	}
}
