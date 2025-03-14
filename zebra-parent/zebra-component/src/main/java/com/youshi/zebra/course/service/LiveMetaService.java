package com.youshi.zebra.course.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.youshi.zebra.core.constants.config.IntConfigKey;
import com.youshi.zebra.core.utils.DateUtil;
import com.youshi.zebra.course.constants.CourseStatus;
import com.youshi.zebra.course.constants.CourseType;
import com.youshi.zebra.course.dao.LiveDAO;
import com.youshi.zebra.course.dao.LiveDAO.LiveStatus;
import com.youshi.zebra.course.dao.LiveMetaDAO;
import com.youshi.zebra.course.dao.LiveMetaDAO.LiveMetaStatus;
import com.youshi.zebra.course.model.LiveMetaModel;
import com.youshi.zebra.course.model.LiveMetaModel.LiveMetaKeys;
import com.youshi.zebra.course.model.LiveModel;
import com.youshi.zebra.course.model.LiveModel.LiveKeys;
import com.youshi.zebra.course.model.OpenCourseMetaAdminView;
import com.youshi.zebra.course.model.OpenCourseView;
import com.youshi.zebra.image.exception.ImageUploadException;
import com.youshi.zebra.image.model.ImageView;
import com.youshi.zebra.image.service.ImageService;
import com.youshi.zebra.order.constants.ProductType;
import com.youshi.zebra.order.service.ProductAdminService;
import com.youshi.zebra.recommend.model.RecommendFeedView;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.service.UserService;




/**
 * 
 * 
 * @author codegen
 * 
 */
@Service
public class LiveMetaService extends AbstractService<Integer, LiveMetaModel>{
	private static final Logger logger = LoggerFactory.getLogger(LiveMetaService.class);
	
	@Autowired
	private LiveMetaDAO liveMetaDAO;
	
	@Autowired
	private LiveDAO liveDAO;
	
	@Autowired
	private CourseLiveService liveService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CourseLiveService courseLiveService;
	
	@Override
	public AbstractDAO<Integer, LiveMetaModel> dao() {
		return liveMetaDAO;
	}
	
	public OpenCourseView getOpenCourseDetail( Integer userId , Integer lmid ) {
		
		LiveMetaModel model = liveMetaDAO.getById(lmid);
		
		LiveModel hased = liveDAO.getByStudentIdAndLmId(userId, lmid);
		
		OpenCourseView result = new OpenCourseView();
		result.setLmId(model.getUuid());
		result.setName(model.getName());
		result.setDesc(model.getDesc());
		result.setImage(new ImageView(imageService.getById(model.getBigImageId())));
		result.setJoinCnt(model.getJoinCount());
		Date parse = DateUtil.parse(model.getOpenTime(), "yyyy-MM-dd hh:mm:ss.S");
		String format = DateUtil.format(parse, "MM/dd hh:mm");
		result.setOpenTime(format);
		result.setPrice(model.getPrice());
		
		result.setSuitableCrowds(model.getSuitableCrowds());
		result.setSubNotes(model.getSubNotes());
		result.setType(model.getType());
		
		if(model.getType()==CourseType.Normal.getValue()) {
			if(hased==null) {
				result.setStatus(1);
			} else {
				if(model.getStatus()==CourseStatus.Finished.getValue()) {
					result.setStatus(3);
				}else {
					result.setStatus(2);
				}
			}
		} else {
			if(hased==null) {
				result.setStatus(0);
			} else {
				if(model.getStatus()==CourseStatus.Finished.getValue()) {
					result.setStatus(3);
				}else {
					result.setStatus(2);
				}
			}
		}
		
		
		return result;
	}
	
	public PageView<RecommendFeedView, String> getOpenCourseList(Integer cursor, int limit) {
		WhereClause params = WhereClause.create();
		params.and().eq(LiveMetaKeys.type.toString(), CourseType.Normal.getValue());
		params.and().notEq(LiveMetaKeys.status.toString(), CourseStatus.Del.getValue());
		PageView<LiveMetaModel, HasUuid<Integer>> pages = getByCursor(cursor, limit, params);
		List<RecommendFeedView> parseFeed = parseFeed(pages.getList());
		String cursorStr = pages.getNextCursor()==null?null: pages.getNextCursor().getUuid();
		PageView<RecommendFeedView, String> result = new PageView<>(parseFeed,cursorStr);
		return result;
	}
	
	public PageView<RecommendFeedView, String> getTeacherCourseList(Integer teacherId,Integer cursor, int limit) {
		WhereClause params = WhereClause.create();
		params.and().eq(LiveMetaKeys.teacher_id.toString(), teacherId);
		params.and().notEq(LiveMetaKeys.status.toString(), CourseStatus.Del.getValue());
		PageView<LiveMetaModel, HasUuid<Integer>> pages = getByCursor(cursor, limit, params);
		List<RecommendFeedView> parseFeed = parseFeed(pages.getList());
		String cursorStr = pages.getNextCursor()==null?null: pages.getNextCursor().getUuid();
		PageView<RecommendFeedView, String> result = new PageView<>(parseFeed,cursorStr);
		return result;
	}
	
	public PageView<RecommendFeedView, String> getPaidOpenCourseList( Integer userId , Integer cursor, int limit) {
		WhereClause params = WhereClause.create();
		params.and().eq(LiveKeys.student_id, userId);
		params.and().eq(LiveKeys.status, 0);
		PageView<LiveModel, HasUuid<Integer>> pages = courseLiveService.getByCursor(cursor, limit, params);
		
		List<Integer> ids = new ArrayList<Integer>();
		for (LiveModel live : pages.getList()) {
			ids.add(live.getLmId());
		}
		
		List<LiveMetaModel> listByIds = getListByIds(ids);
		
		List<RecommendFeedView> parseFeed = parseFeed(listByIds);
		String cursorStr = pages.getNextCursor()==null?null: pages.getNextCursor().getUuid();
		PageView<RecommendFeedView, String> result = new PageView<>(parseFeed,cursorStr);
		return result;
	}
	
	
	private List<RecommendFeedView> parseFeed( List<LiveMetaModel> list ) {
		List<RecommendFeedView> newList = new ArrayList<RecommendFeedView>();
		for (LiveMetaModel model : list) {
			RecommendFeedView view = getView(model);
			newList.add(view);
		}
		return newList;
	}
	
	private RecommendFeedView getView(LiveMetaModel live){
		RecommendFeedView view = new RecommendFeedView();
		view.setId(live.getUuid());
		view.setTitle(live.getName());
		view.setCount(live.getJoinCount());
		view.setDesc(live.getDesc());
		Date parse = DateUtil.parse(live.getOpenTime(), "yyyy-MM-dd hh:mm:ss.S");
		String format = DateUtil.format(parse, "MM/dd hh:mm");
		view.setOpenTime(format);
		view.setImage(new ImageView(imageService.getById(live.getImageId())));
		view.setStatus(live.getStatus());
		return view;
	}
	
	
	
	public void addLive( Integer userId , Integer lmId ) {
		
		LiveMetaModel liveMeta = liveMetaDAO.getById(lmId);

		liveDAO.insert(lmId, userId, liveMeta.getType(), liveMeta.getOpenTime(),
				liveMeta.getData(), LiveStatus.Normal, new Date().getTime());
		
		logger.info(" OpenCourse freeRial Success userId:{} , lmId:{} " ,userId , lmId  );
		
	}
	
	@Autowired
	private ProductAdminService productAdminService;
	
	public void addLiveMeta(
			Integer adminId,Integer teacher,String name, String desc, MultipartFile imageFile,
			MultipartFile bigImageFile,MultipartFile shareImage,Integer level, 
			Integer type,String openTime,Integer joinCnt,String shareDesc,String shareUrl,
			String price, List<String> subNotes, String suitableCrowds) {
		logger.info(" LiveMetaService.addLiveMeta() In success . ");
		Integer imageId = null;
		if(imageFile!=null) {
			try {
				imageId = imageService.createImage(adminId, imageFile.getBytes());
			} catch(IOException e) {
				throw new ImageUploadException(e);
			}
		}
		
		Integer bigImageId = null;
		if(bigImageFile!=null) {
			try {
				bigImageId = imageService.createImage(adminId, bigImageFile.getBytes());
			} catch(IOException e) {
				throw new ImageUploadException(e);
			}
		}
		
		Integer shareImageId = null;
		if(shareImage!=null) {
			try {
				shareImageId = imageService.createImage(adminId, shareImage.getBytes());
			} catch(IOException e) {
				throw new ImageUploadException(e);
			}
		}
		
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put(LiveMetaKeys.name.name(), name);
		dataMap.put(LiveMetaKeys.desc.name(), desc);
		dataMap.put(LiveMetaKeys.image_id.name(), imageId);
		dataMap.put(LiveMetaKeys.big_image_id.name(), bigImageId);
		dataMap.put(LiveMetaKeys.level.name(), level);
		dataMap.put(LiveMetaKeys.price.name(), price);
		dataMap.put(LiveMetaKeys.sub_notes.name(), subNotes);
		dataMap.put(LiveMetaKeys.suitable_crowds.name(), suitableCrowds);
		
		dataMap.put(LiveMetaKeys.open_time.name(), openTime);
		dataMap.put(LiveMetaKeys.join_count.name(), joinCnt);
		dataMap.put(LiveMetaKeys.share_image_id.name(), shareImageId);
		dataMap.put(LiveMetaKeys.share_brief.name(), shareDesc);
		dataMap.put(LiveMetaKeys.share_jump_url.name(), shareUrl);
		
		String data = DoradoMapperUtils.toJSON(dataMap);
		
		int insert = liveMetaDAO.insert(type, openTime,teacher , data, LiveMetaStatus.Normal, System.currentTimeMillis());
		
		// add by wangsch at 2017-11-14 11:17:56
		int cost = Integer.parseInt(price);
		productAdminService.doCreateProductCost(cost, cost, ProductType.OPEN_COURSE, insert);
		// END
		
		
		logger.info(" live_meta Insert success . lmId:{} ",insert);
	}
	
	public void updateJoinCnt( Integer lmId , Integer joinCnt ) {
		logger.info(" LiveMetaService.updateJoinCot() In success . ");
		LiveMetaModel model = liveMetaDAO.getById(lmId);
		Map<String, Object> resolvedData = model.resolvedData();
		resolvedData.put(LiveMetaKeys.join_count.name(), joinCnt);
		
		String newData = DoradoMapperUtils.toJSON(resolvedData);
		String oldData = DoradoMapperUtils.toJSON(model.resolvedData());
		liveMetaDAO.updateData(lmId, newData, oldData);
		logger.info(" live_meta Data Update success . lmId:{} , joinCnt:{} ",joinCnt);
	}
	
	public void updateStatus(Integer lmId , CourseStatus status ) {
		liveMetaDAO.updateStatus(lmId, status.getValue());
		logger.info(" live_meta Status Update success . lmId:{} ",lmId);
	}
	
	
	
	public void editLiveMeta(
			Integer id,
			Integer adminId, String name, String desc, MultipartFile imageFile,
			MultipartFile bigImageFile,MultipartFile shareImage,Integer level, 
			Integer type,String openTime,Integer joinCnt,String shareDesc,String shareUrl,
			String price, List<String> subNotes, String suitableCrowds) {
		logger.info(" LiveMetaService.editLiveMeta() In success . ");
		
		LiveMetaModel model = liveMetaDAO.getById(id);
		
		Integer imageId = model.getImageId();
		if(imageFile!=null) {
			try {
				imageId = imageService.createImage(adminId, imageFile.getBytes());
			} catch(IOException e) {
				throw new ImageUploadException(e);
			}
		}
		
		Integer bigImageId = model.getBigImageId();
		if(bigImageFile!=null) {
			try {
				bigImageId = imageService.createImage(adminId, bigImageFile.getBytes());
			} catch(IOException e) {
				throw new ImageUploadException(e);
			}
		}
		
		Integer shareImageId = model.getShareImageId();
		if(shareImage!=null) {
			try {
				shareImageId = imageService.createImage(adminId, shareImage.getBytes());
			} catch(IOException e) {
				throw new ImageUploadException(e);
			}
		}
		
		Map<String, Object> dataMap = model.resolvedData();
		dataMap.put(LiveMetaKeys.name.name(), name);
		dataMap.put(LiveMetaKeys.desc.name(), desc);
		dataMap.put(LiveMetaKeys.image_id.name(), imageId);
		dataMap.put(LiveMetaKeys.big_image_id.name(), bigImageId);
		dataMap.put(LiveMetaKeys.level.name(), level);
		dataMap.put(LiveMetaKeys.price.name(), price);
		dataMap.put(LiveMetaKeys.sub_notes.name(), subNotes);
		dataMap.put(LiveMetaKeys.suitable_crowds.name(), suitableCrowds);
		
		dataMap.put(LiveMetaKeys.open_time.name(), openTime);
		dataMap.put(LiveMetaKeys.join_count.name(), joinCnt);
		dataMap.put(LiveMetaKeys.share_image_id.name(), shareImageId);
		if(StringUtils.isNotBlank(shareDesc))dataMap.put(LiveMetaKeys.share_brief.name(), shareDesc);
		if(StringUtils.isNotBlank(shareUrl))dataMap.put(LiveMetaKeys.share_jump_url.name(), shareUrl);
		
		String data = DoradoMapperUtils.toJSON(dataMap);
		
		int insert = liveMetaDAO.updateMeta(id, openTime, data);
		
		
		// add by wangsch at 2017-11-14 11:17:56
		int cost = Integer.parseInt(price);
		productAdminService.doUpdateProductCost(cost, cost, ProductType.OPEN_COURSE, insert);
		// END
		
		logger.info(" live_meta edit success . lmId:{} ",insert);
	}
	
	
	
	
	
	public PageView<OpenCourseMetaAdminView, String> getPageByCorsor( Integer cursor, Integer limit ) {
		 WhereClause params = WhereClause.create();
		 //params.and().notEq("status", LiveMetaStatus.Del.getValue());
		 PageView<LiveMetaModel, HasUuid<Integer>> pages = getByCursor(cursor, limit, params);
		 List<OpenCourseMetaAdminView> buildAdminView = buildAdminView(pages.getList());
		 String cursorStr = pages.getNextCursor()==null?null: pages.getNextCursor().getUuid();
		 PageView<OpenCourseMetaAdminView, String> pageView = new PageView<>(buildAdminView, cursorStr);
		 return pageView;
	}
	
	private List<OpenCourseMetaAdminView> buildAdminView( List<LiveMetaModel> list ){
		List<OpenCourseMetaAdminView> viewList = new ArrayList<OpenCourseMetaAdminView>();
		for (LiveMetaModel model : list) {
			
			Integer imageId = model.getImageId();
			if(imageId==null) {
				imageId = IntConfigKey.DefaultHeadImageId.get();
			}
			ImageView imageView = new ImageView(imageService.getById(imageId));
			OpenCourseMetaAdminView view = new OpenCourseMetaAdminView(model, imageView);
			
			view.setUser(userService.getById(model.getTeacherId()));
			viewList.add(view);
		}
		return viewList;
	}
	
	public PageView<OpenCourseMetaAdminView, String> getPaidByCorsor(Integer cursor, Integer limit) {
		WhereClause params = WhereClause.create();
		params.and().eq("type", CourseType.Normal.getValue());
		PageView<LiveModel, HasUuid<Integer>> pages = liveService.getByCursor(cursor, limit, params);
		
		List<OpenCourseMetaAdminView> buildAdminView = buildAdminPaidView(pages.getList());
		String cursorStr = pages.getNextCursor()==null?null: pages.getNextCursor().getUuid();
		PageView<OpenCourseMetaAdminView, String> pageView = new PageView<>(buildAdminView, cursorStr);
		
		return pageView;
	}
	
	private List<OpenCourseMetaAdminView> buildAdminPaidView( List<LiveModel> list ){
		List<OpenCourseMetaAdminView> viewList = new ArrayList<OpenCourseMetaAdminView>();
		for (LiveModel model : list) {
			
			LiveMetaModel byId = getById(model.getLmId());
			ImageView imageView = new ImageView(imageService.getById(byId.getImageId()));
			
			OpenCourseMetaAdminView view = new OpenCourseMetaAdminView(byId, imageView);
			
			UserModel user = userService.getById(model.getStudentId());
			view.setUser(user);
			viewList.add(view);
		}
		return viewList;
	}

	public OpenCourseMetaAdminView getAdminOpenCourseDetail(Integer lmid) {
		LiveMetaModel model = getById(lmid);
		
		ImageView imageView = new ImageView(imageService.getById(model.getImageId()));
		OpenCourseMetaAdminView view = new OpenCourseMetaAdminView(model, imageView);
		UserModel user = userService.getById(model.getTeacherId());
		view.setUser(user);
		
		return view;
	}

	public void unshelve(Integer adminId, Integer cmId) {
		liveMetaDAO.updateStatus(cmId, CourseStatus.Del.getValue());
		logger.info(" live_meta edit status success . lmId:{} ");
	}
	
	public void shelve(Integer adminId, Integer cmId) {
		liveMetaDAO.updateStatus(cmId, LiveMetaStatus.Normal.getValue());
		logger.info(" live_meta edit status success . lmId:{} ");
	}
	
	
}