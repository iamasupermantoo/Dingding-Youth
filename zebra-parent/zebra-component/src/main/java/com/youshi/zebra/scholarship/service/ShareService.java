package com.youshi.zebra.scholarship.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.constants.InProduction;
import com.dorado.framework.crud.model.util.ModelUtils;
import com.github.phantomthief.util.ObjectMapperUtils;
import com.youshi.zebra.core.exception.EntityNotFoundException;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.course.model.LiveMetaModel;
import com.youshi.zebra.course.model.LiveMetaModel.LiveMetaKeys;
import com.youshi.zebra.course.service.CourseMetaService;
import com.youshi.zebra.course.service.CourseService;
import com.youshi.zebra.course.service.LiveMetaService;
import com.youshi.zebra.image.model.ImageModel;
import com.youshi.zebra.image.service.ImageService;
import com.youshi.zebra.image.utils.ImageUtils;
import com.youshi.zebra.scholarship.WxShareInfo;
import com.youshi.zebra.scholarship.constants.ShareConstant;
import com.youshi.zebra.scholarship.dao.ScholarshipRedisDAO;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.service.UserService;

/**
 * 
 * @author wangsch
 * @date 2017年11月10日
 */
@Service
public class ShareService {
	/**
	 * 
	 */
	private static final int SHARE_INCR_AMOUNT_5_RMB = 500;

	private static final Logger logger = LoggerFactory.getLogger(ShareService.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private CourseMetaService courseMetaService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private LiveMetaService liveMetaService;
	
	@Autowired
	private ScholarshipService scholarshipService;
	
	@Autowired
	private ScholarshipRedisDAO scholarshipRedisDAO;
	
	public WxShareInfo getWxShareInfo(Integer userId, Integer liveMetaId, Integer courseId) {
		UserModel user = userService.getById(userId);
		
		WxShareInfo info = null;
		if(courseId != null) {
			CourseModel course = courseService.getById(courseId);
			if(course == null) {
				throw new EntityNotFoundException();
			}
			CourseMetaModel cm = courseMetaService.getById(course.getCmId());
			if(cm == null) {
				throw new EntityNotFoundException();
			}
			info = buildCourseMetaShareInfo(cm, user);
			
		} else if(liveMetaId != null) {
			LiveMetaModel liveMeta = liveMetaService.getById(liveMetaId);
			if(liveMeta == null) {
				throw new EntityNotFoundException();
			}
			info = buildLiveMetaShareInfo(liveMeta, user);
		}
		
		// 测试环境，mock容错
		if(!InProduction.get() && info != null && StringUtils.isEmpty(info.getBrief())) {
			info.setBrief("这是分享内容简介，简介简介");
			info.setPicUrl("http://img.s.ud09.com/udan_wxs.png");
			info.setTitle("这是分享标题，标题标题");
			info.setUrl("http://www.baidu.com");
		}
		
		logger.info("Share info: {}", ObjectMapperUtils.toJSON(info));
		return info;
	}
	
	public void shareOK(Integer userId, Integer liveMetaId, Integer courseId, Integer lessonId) {
		Long res = 0L;
		if(liveMetaId != null) {
			res = scholarshipRedisDAO.addLive(userId, liveMetaId);
		} else if(courseId != null && lessonId != null) {
			res = scholarshipRedisDAO.addLesson(userId, courseId, lessonId);
		}
		
		// 等于1，说明第一次添加，则加奖学金
		boolean addScholarship = res == 1;
		if(addScholarship) {
			scholarshipService.incrTotalAmount(userId, SHARE_INCR_AMOUNT_5_RMB);
		}
		
		logger.info("User share ok. UserId: {}, add scholarship : {}", userId, addScholarship);
	}
	
	private WxShareInfo buildLiveMetaShareInfo(LiveMetaModel live, UserModel user) {
		String title = ModelUtils.getString(live, LiveMetaKeys.share_brief);
		String brief = ModelUtils.getString(live, LiveMetaKeys.share_brief);
		Integer imageId = ModelUtils.getInt(live, LiveMetaKeys.share_image_id);
		String picUrl = null;
		if(imageId != null) {
			ImageModel image = imageService.getById(imageId);
			picUrl = ImageUtils.getImageUrl(image, 200, 200);
		}
		String jumpUrl = ModelUtils.getString(live, LiveMetaKeys.share_jump_url);
		
		if(StringUtils.isNotEmpty(brief)) {
			brief = brief.replace("\n", "");
		}
		
		WxShareInfo info = new WxShareInfo(ShareConstant.WX_APP_ID, ShareConstant.WX_SECRET, jumpUrl, 
				title, brief, picUrl);
		return info;
	}
	
	private WxShareInfo buildCourseMetaShareInfo(CourseMetaModel cm, UserModel user) {
		String title = "分享";
		String brief = ModelUtils.getString(cm, LiveMetaKeys.share_brief);
		Integer imageId = ModelUtils.getInt(cm, LiveMetaKeys.share_image_id);
		String picUrl = null;
		if(imageId != null) {
			ImageModel image = imageService.getById(imageId);
			picUrl = ImageUtils.getImageUrl(image, 200, 200);
		}
		String jumpUrl = ModelUtils.getString(cm, LiveMetaKeys.share_jump_url);
		
		if(StringUtils.isNotEmpty(brief)) {
			brief = brief.replace("\n", "");
		}
		
		WxShareInfo info = new WxShareInfo(ShareConstant.WX_APP_ID, ShareConstant.WX_SECRET, jumpUrl, 
				title, brief, picUrl);
		return info;
	}
}
