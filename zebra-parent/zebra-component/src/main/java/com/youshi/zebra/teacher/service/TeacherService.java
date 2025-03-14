package com.youshi.zebra.teacher.service;

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
import com.dorado.framework.crud.model.HasData;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.crud.service.AbstractService;
import com.dorado.framework.event.utils.PerfUtils;
import com.dorado.framework.model.PageView;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.ecyrd.speed4j.StopWatch;
import com.github.phantomthief.util.ObjectMapperUtils;
import com.youshi.zebra.connect.constant.ConnectType;
import com.youshi.zebra.core.constants.CommonKey;
import com.youshi.zebra.core.constants.config.IntConfigKey;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.core.utils.DateUtil;
import com.youshi.zebra.image.exception.ImageUploadException;
import com.youshi.zebra.image.model.ImageModel;
import com.youshi.zebra.image.model.ImageView;
import com.youshi.zebra.image.service.ImageService;
import com.youshi.zebra.mobile.exception.MobileAlreadyRegisteredException;
import com.youshi.zebra.mobile.service.MobileCodeService;
import com.youshi.zebra.passport.service.UserPassportService;
import com.youshi.zebra.register.service.ConnectBindService;
import com.youshi.zebra.teacher.dao.TeacherDAO;
import com.youshi.zebra.teacher.model.TeacherModel;
import com.youshi.zebra.teacher.model.TeacherModel.TeacherKeys;
import com.youshi.zebra.teacher.model.TeacherView;
import com.youshi.zebra.user.constant.UserStatus;
import com.youshi.zebra.user.constant.UserType;
import com.youshi.zebra.user.dao.UserDAO;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.model.UserModel.UserKey;
import com.youshi.zebra.user.service.UserService;

/**
 * 
 * @author wangsch
 * @date 2017年2月13日
 */
@Service
public class TeacherService extends AbstractService<Integer, TeacherModel> {
	private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);
	
	@Autowired
	private TeacherDAO teacherDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private UserPassportService passportService;
	
	@Autowired
	private MobileCodeService mobileCodeService;
	
	@Autowired
	private ConnectBindService connectBindService;
	
	@Override
	protected AbstractDAO<Integer, TeacherModel> dao() {
		return teacherDAO;
	}

	// TODO 根据具体查询条件，也许需要改查teacher表
	public PageView<UserModel, HasUuid<Integer>> queryTeachers(Integer tid, UserStatus status, 
			Integer cursor, Integer limit) {
		WhereClause params = WhereClause.create().and().eq(UserKey.type, UserType.Teacher.getValue());
		if(tid != null) {
			params.and().eq(TeacherKeys.id, tid);
		}
		if(status != null) {
			params.and().eq(TeacherKeys.status, status.getValue());
		}
		
		PageView<UserModel, HasUuid<Integer>> result = userService.getByCursor(cursor, limit, params);
		
		return result;
	}
	
	
	public PageView<TeacherView, String> getTeacherList(
			Integer cursor, Integer limit) {
		WhereClause params = WhereClause.create().and().eq(UserKey.type, UserType.Teacher.getValue());	
		params.and().eq(TeacherKeys.status, UserStatus.Normal.getValue());
		
		PageView<UserModel, HasUuid<Integer>> pages = userService.getByCursor(cursor, limit, params);
			
		List<TeacherView> buildTeacher = buildTeacher(pages.getList());
		
		String cursorStr = pages.getNextCursor()==null?null: pages.getNextCursor().getUuid();
		PageView<TeacherView, String> result = new PageView<TeacherView, String>(buildTeacher, cursorStr);
		
		return result;
	}
	
	private List<TeacherView> buildTeacher(List<UserModel> list) {
		List<TeacherView> views = new ArrayList<TeacherView>();
		for (UserModel model : list) {
			Integer imageId = model.getImageId();
			ImageView image = null;
			if(imageId!=null) {
				image = new ImageView(imageService.getById(imageId));
			}
			views.add(new TeacherView(model,image ));
		}
		return views;
	}
	
	
	
	/**
	 * @param name
	 * @param desc
	 * @param gender
	 * @param headImageFile
	 * @param cmIds
	 */
	public void create(String mobile, String password, String name, String email, String desc, Integer gender, String birthday,
			MultipartFile headImageFile , MultipartFile imageFile) {

		Integer userId = registerTeacher(name, gender, UserType.Student, 
				mobile, password, StringUtils.EMPTY, birthday ,desc , headImageFile , imageFile);
		
		if (userId != null && userId != 0) {
			connectBindService.bindConnect(userId, ConnectType.Mobile, null, null, mobile);
		}
		
		
		
		// 教师Data
		Map<String, Object> dataMap = new HashMap<>();
		//if(imageId!=null)dataMap.put(TeacherKeys.image_id.name(), imageId);
		if(StringUtils.isNotBlank(name))dataMap.put(TeacherKeys.name.name(), name);
		if(StringUtils.isNotBlank(desc))dataMap.put(TeacherKeys.desc.name(), desc);
		
		// 教师表
		int c = teacherDAO.insert(userId, HasData.EMPTY_DATA, UserStatus.Normal, System.currentTimeMillis());
		DAOUtils.checkAffectRows(c);
		
		logger.info("Teacher create succ. tid: {}", userId);
	}
	
	/**
	 * @param name
	 * @param student
	 * @param mobile
	 * @param password
	 * @param empty
	 * @param headImageFile
	 * @return
	 */
	private Integer registerTeacher(String name, Integer gender, UserType student, String mobile, String password, String empty,
			String birthday,String desc,
		MultipartFile image, MultipartFile imageFile) {
		StopWatch watcher = PerfUtils.getWatcher("RegisterService.registerTeacher");
		if (passportService.isRegisterd(mobile)) {
			logger.warn("Mobile already REGISTERED, mobile: {}", mobile);
			throw new MobileAlreadyRegisteredException();
		}
		
        // 上传头像
        Integer headImageId = IntConfigKey.DefaultHeadImageId.get();
		try {
			headImageId = createImage(image);
		} catch (Exception e) {
			throw new ImageUploadException();
		}
		// 上传头像
        Integer imageId = IntConfigKey.DefaultHeadImageId.get();
		try {
			imageId = createImage(imageFile);
		} catch (Exception e) {
			throw new ImageUploadException();
		}
		
		// 创建用户信息和账号
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put(UserKey.head_image_id.name(), headImageId);
		dataMap.put(UserKey.gender.name(), gender);
		dataMap.put(UserKey.mobile.name(), mobile);
		dataMap.put(UserKey.desc.name(), desc);
		if(imageId!=null)dataMap.put(TeacherKeys.image_id.name(), imageId);
		
		if(StringUtils.isNotBlank(birthday)) {
			dataMap.put(UserKey.birthday.name(), birthday);
			Date parse = DateUtil.parse(birthday, "yyyy-MM-dd");
			int age = DateUtil.getAge(parse);
			dataMap.put(UserKey.age.name(), age);
		}
		
		// common keys
		CommonKey.inject(dataMap);
        int userId = userDAO.insert(name, UserType.Teacher, StringUtils.EMPTY, ObjectMapperUtils.toJSON(dataMap), 
        		UserStatus.Normal, System.currentTimeMillis());
        passportService.createAccount(userId, mobile, password);
        
        logger.info("User mobile register OK. userId: {}, mobile: {}", userId, mobile);
        watcher.stop();
        return userId;
	}
	
	
	public void edit(Integer tid, String name, String desc, Integer gender, String birthday,
			MultipartFile headImageFile, MultipartFile imageFile ) {
		UserModel user = userService.expectUserType(tid, UserType.Teacher);
		userService.clearCache(tid);
		
		Integer headImageId = user.getHeadImageId();
		try {
			headImageId = createImage(headImageFile);
		} catch (IOException e) {
			throw new ImageUploadException();
		}
		// 上传头像
        Integer imageId = user.getImageId();
		try {
			imageId = createImage(imageFile);
		} catch (Exception e) {
			throw new ImageUploadException();
		}
		
		Map<String, Object> dataMap = user.resolvedData();
		dataMap.put(UserKey.name.name(), name);
		dataMap.put(UserKey.gender.name(), gender);
		dataMap.put(UserKey.desc.name(), desc);
		dataMap.put(UserKey.head_image_id.name(), headImageId);
		dataMap.put(UserKey.desc.name(), desc);
		if(imageId!=null)dataMap.put(TeacherKeys.image_id.name(), imageId);
		if(StringUtils.isNotBlank(birthday)) {
			dataMap.put(UserKey.birthday.name(), birthday);
			Date parse = DateUtil.parse(birthday, "yyyy-MM-dd");
			int age = DateUtil.getAge(parse);
			dataMap.put(UserKey.age.name(), age);
		}
		
		String signature = "";
		String data = DoradoMapperUtils.toJSON(dataMap);
		userService.update(tid, name, signature, data);

	}

	public void update(Integer tid, String name, String desc, Integer gender, String age,
			MultipartFile headImageFile, MultipartFile imageFile  ,List<Integer> cmIds) {
		UserModel user = userService.expectUserType(tid, UserType.Teacher);
		userService.clearCache(tid);
		
		Integer headImageId = null;
		try {
			headImageId = createImage(headImageFile);
		} catch (IOException e) {
			throw new ImageUploadException();
		}
		
		Map<String, Object> dataMap = user.resolvedData();
		dataMap.put(TeacherKeys.name.name(), name);
		dataMap.put(TeacherKeys.gender.name(), gender);
		dataMap.put(TeacherKeys.desc.name(), desc);
		dataMap.put(TeacherKeys.head_image_id.name(), headImageId);
		
		String signature = "";
		String data = DoradoMapperUtils.toJSON(dataMap);
		userService.update(tid, name, signature, data);
		
		TeacherModel teacher = teacherDAO.getById(tid);
		Map<String, Object> teacherData = teacher.resolvedData();
		// 教师Data
		if(imageFile!=null) {
			Integer imageId = null;
			try {
				imageId = createImage(imageFile);
			} catch (Exception e) {
				throw new ImageUploadException();
			}
			teacherData.put(TeacherKeys.image_id.name(), imageId);
		}
		if(StringUtils.isNotBlank(name))teacherData.put(TeacherKeys.name.name(), name);
		if(StringUtils.isNotBlank(desc))teacherData.put(TeacherKeys.desc.name(), desc);
		if(StringUtils.isNotBlank(age))teacherData.put(TeacherKeys.age.name(), age);
		data=DoradoMapperUtils.toJSON(teacherData);
		int c = teacherDAO.update(tid, data);
		DAOUtils.checkAffectRows(c);
	}
	
	
	
	private Integer createImage(MultipartFile image) throws IOException {
		if(image == null || image.isEmpty()||image.getBytes().length == 0) {
			return IntConfigKey.DefaultHeadImageId.get();
		}
		
		Integer headImageId = imageService.createImage(WebRequestContext.getUserId(),
				image.getBytes());
		return headImageId;
	}
}
