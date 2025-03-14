package com.youshi.zebra.course.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.crud.service.AbstractService;
import com.dorado.framework.model.PageView;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.youshi.zebra.book.constants.BookStatus;
import com.youshi.zebra.book.model.BookModel;
import com.youshi.zebra.book.service.BookService;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.course.constants.CourseMetaStatus;
import com.youshi.zebra.course.dao.CourseMetaDAO;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.model.CourseMetaModel.CourseMetaKeys;
import com.youshi.zebra.exception.base.DoradoRuntimeException;
import com.youshi.zebra.exception.common.EntityNotNormalException;
import com.youshi.zebra.image.exception.ImageUploadException;
import com.youshi.zebra.image.service.ImageService;
import com.youshi.zebra.user.constant.UserStatus;
import com.youshi.zebra.user.constant.UserType;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.service.UserService;

/**
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
@Service
public class CourseMetaService extends AbstractService<Integer, CourseMetaModel>{
	private static final Logger logger = LoggerFactory.getLogger(CourseMetaService.class);
	
	@Autowired
	private CourseMetaDAO courseMetaDAO;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private BookService bookService;
	
	@Override
	protected AbstractDAO<Integer, CourseMetaModel> dao() {
		return courseMetaDAO;
	}
	
	public PageView<CourseMetaModel, HasUuid<Integer>> getCourseMetas(Integer cmId, CourseMetaStatus status,
			Integer cursor, Integer limit) {
		WhereClause params = WhereClause.create();
		if(cmId != null) {
			params.and().eq("id", cmId);
		}
		if(status != null) {
			params.and().eq("status", status.getValue());
		}

		PageView<CourseMetaModel, HasUuid<Integer>> page = getByCursor(cursor, limit, params);
		
		return page;
	}
	
	public int add(Integer adminId, Integer bookId, String name, String desc, MultipartFile imageFile, MultipartFile bigImageFile,
			Integer level, Integer type,
			String price, List<String> subNotes, String suitableCrowds, 
			String shareJumpUrl, MultipartFile shareImage, String shareBrief) {
		BookModel book = bookService.getById(bookId);
		
		Integer imageId = null;
		try {
			imageId = imageService.createImage(adminId, imageFile.getBytes());
		} catch(IOException e) {
			throw new ImageUploadException(e);
		}
		
		Integer bigImageId = null;
		try {
			bigImageId = imageService.createImage(adminId, bigImageFile.getBytes());
		} catch(IOException e) {
			throw new ImageUploadException(e);
		}
		
		Integer shareImageId = null;
		if(shareImage != null) {
			try {
				shareImageId = imageService.createImage(adminId, shareImage.getBytes());
			} catch(IOException e) {
				throw new ImageUploadException(e);
			}
		}
		
		
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put(CourseMetaKeys.name.name(), name);
		dataMap.put(CourseMetaKeys.desc.name(), desc);
		dataMap.put(CourseMetaKeys.image_id.name(), imageId);
		dataMap.put(CourseMetaKeys.big_image_id.name(), bigImageId);
		dataMap.put(CourseMetaKeys.level.name(), level);
		dataMap.put(CourseMetaKeys.price.name(), price);
		dataMap.put(CourseMetaKeys.total_cnt.name(), book.getTotalCnt());
		dataMap.put(CourseMetaKeys.sub_notes.name(), subNotes);
		dataMap.put(CourseMetaKeys.share_jump_url.name(), shareJumpUrl);
		dataMap.put(CourseMetaKeys.share_image_id.name(), shareImageId);
		dataMap.put(CourseMetaKeys.share_brief.name(), shareBrief);
		
		String data = DoradoMapperUtils.toJSON(dataMap);
		Number id = courseMetaDAO.insert(bookId, data, CourseMetaStatus.Normal, type, System.currentTimeMillis());
		
		bookService.updateBookStatus(bookId, BookStatus.IN_USE);
		
		logger.info("Add course succ. cmid: {}, adminId: {}", id, adminId);
		return id.intValue();
	}
	
	// TODO 具体修改逻辑，可以修改哪些字段待定
	public void modify(Integer adminId, Integer cmId, String name, String desc, MultipartFile imageFile, MultipartFile bigImageFile, 
			Integer level, Integer type,
			String price, List<String> subNotes, String suitableCrowds ,Integer joinCnt,
			String shareJumpUrl, MultipartFile shareImage, String shareBrief
			) {
		
		CourseMetaModel courseMeta = getById(cmId);
		CourseMetaStatus status = CourseMetaStatus.fromValue(courseMeta.getStatus());
		if(status != CourseMetaStatus.Normal && status != CourseMetaStatus.Pending) {
			throw new EntityNotNormalException();
		}
		
		Integer imageId = courseMeta.getImageId();
		try {
			if(imageFile != null) {
				imageId = imageService.createImage(adminId, imageFile.getBytes());
			}
			
		} catch(IOException e) {
			throw new ImageUploadException(e);
		}
		
		Integer bigImageId = courseMeta.getBigImageId();
		try {
			if(bigImageFile != null) {
				bigImageId = imageService.createImage(adminId, bigImageFile.getBytes());
			}
		} catch(IOException e) {
			throw new ImageUploadException(e);
		}
		
		Integer shareImageId = courseMeta.getShareImageId();
		if(shareImage != null) {
			try {
				shareImageId = imageService.createImage(adminId, shareImage.getBytes());
			} catch(IOException e) {
				throw new ImageUploadException(e);
			}
		}
		
		Map<String, Object> dataMap = courseMeta.resolvedData();
		dataMap.put(CourseMetaKeys.name.name(), name);
		dataMap.put(CourseMetaKeys.desc.name(), desc);
		dataMap.put(CourseMetaKeys.image_id.name(), imageId);
		dataMap.put(CourseMetaKeys.big_image_id.name(), bigImageId);
		dataMap.put(CourseMetaKeys.level.name(), level);
		dataMap.put(CourseMetaKeys.price.name(), price);
		dataMap.put(CourseMetaKeys.sub_notes.name(), subNotes);
		dataMap.put(CourseMetaKeys.suitable_crowds.name(), suitableCrowds);
		dataMap.put(CourseMetaKeys.share_jump_url.name(), shareJumpUrl);
		dataMap.put(CourseMetaKeys.share_image_id.name(), shareImageId);
		dataMap.put(CourseMetaKeys.share_brief.name(), shareBrief);
		
		if(joinCnt!=null)dataMap.put(CourseMetaKeys.join_count.name(), joinCnt);
		
		int c = courseMetaDAO.updateData(cmId, DoradoMapperUtils.toJSON(dataMap), 
				courseMeta.getData());
		DAOUtils.checkAffectRows(c);
	}
	
	public void shelve(Integer adminId, Integer cmId) {
		clearCache(cmId);
		
		int c = courseMetaDAO.setStatus(cmId, CourseMetaStatus.Normal);
		DAOUtils.checkAffectRows(c);
	}
	
	public void unshelve(Integer adminId, Integer cmId) {
		clearCache(cmId);
		
		int c = courseMetaDAO.setStatus(cmId, CourseMetaStatus.Unshelved);
		DAOUtils.checkAffectRows(c);
	}


	/**
	 * @param teacherId
	 * @return
	 */
	private UserModel validateTeacher(Integer teacherId) {
		UserModel user = userService.getById(teacherId);
		UserType userType = UserType.fromValue(user.getType());
		if(userType != UserType.Teacher) {
			logger.error("Validate teacher FAIL, UserType TEACHER required but found: {}, uid: {}", 
					userType, user.getId());
			throw new DoradoRuntimeException();
		}
		if(user.getStatus() != UserStatus.Normal.getValue()) {
			throw new EntityNotNormalException();
		}
		return user;
	}
}
