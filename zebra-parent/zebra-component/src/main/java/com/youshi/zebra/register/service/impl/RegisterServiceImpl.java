package com.youshi.zebra.register.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dorado.framework.event.utils.PerfUtils;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.ecyrd.speed4j.StopWatch;
import com.github.phantomthief.util.ObjectMapperUtils;
import com.youshi.zebra.core.constants.CommonKey;
import com.youshi.zebra.core.constants.config.IntConfigKey;
import com.youshi.zebra.image.exception.ImageUploadException;
import com.youshi.zebra.image.service.ImageService;
import com.youshi.zebra.mobile.constants.MobileCodeStatus;
import com.youshi.zebra.mobile.dao.MobileInfoDAO;
import com.youshi.zebra.mobile.exception.MobileAlreadyRegisteredException;
import com.youshi.zebra.mobile.exception.code.MobileCodeNotVerifyedException;
import com.youshi.zebra.mobile.model.MobileInfo;
import com.youshi.zebra.mobile.service.MobileCodeService;
import com.youshi.zebra.passport.service.UserPassportService;
import com.youshi.zebra.register.service.RegisterService;
import com.youshi.zebra.user.constant.UserStatus;
import com.youshi.zebra.user.constant.UserType;
import com.youshi.zebra.user.dao.UserDAO;
import com.youshi.zebra.user.model.UserModel.UserKey;

/**
 * 注册业务
 * 
 * Date: May 10, 2016
 * 
 * @author wangsch
 *
 */
@Service
public class RegisterServiceImpl implements RegisterService {
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());
	    
    @Autowired
    private UserDAO userDAO;
    
    @Autowired
    private MobileInfoDAO mobileDAO;
    
    @Autowired
    private UserPassportService passportService;
    
    @Autowired
    private MobileCodeService mobileCodeService;
    
    @Autowired
    private ImageService imageService;
    
	@Override
	public Integer registerMobile(String username, UserType userType,
			String mobile, String password, String signature,
			MultipartFile image, String birthday) {
		StopWatch watcher = PerfUtils.getWatcher("RegisterService.registerMobile");
		if (passportService.isRegisterd(mobile)) {
			logger.warn("Mobile already REGISTERED, mobile: {}", mobile);
			throw new MobileAlreadyRegisteredException();
		}
		
		// 检测验证码状态
		MobileInfo mobileInfo = mobileDAO.getRegisterCode(mobile);
        if (mobileInfo == null 
        		|| MobileCodeStatus.fromValue(mobileInfo.getStatus()) != MobileCodeStatus.MobileCodeVerified) {
            logger.warn("Mobile register code NOT VERIFIED, mobile: {}", mobile);
            throw new MobileCodeNotVerifyedException();
        }
		
        // 上传头像
        Integer headImageId = null;
		try {
			headImageId = createImage(image);
		} catch (Exception e) {
			throw new ImageUploadException();
		}
		
		// 创建用户信息和账号
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put(UserKey.head_image_id.name(), headImageId);
		dataMap.put(UserKey.mobile.name(), mobile);
		dataMap.put(UserKey.birthday.name(), birthday);
		// common keys
		CommonKey.inject(dataMap);
        int userId = userDAO.insert(username, userType, signature, ObjectMapperUtils.toJSON(dataMap), 
        		UserStatus.Normal, System.currentTimeMillis());
        passportService.createAccount(userId, mobile, password);
        
        // 验证码状态
        mobileCodeService.setPhoneStatus(mobile, MobileCodeStatus.MobileRegistered);
        
        logger.info("User mobile register OK. userId: {}, mobile: {}", userId, mobile);
        watcher.stop();
        return userId;
    }
	

	// ------------------------------------------------utils method----------------------------------------------
	/**
	 * 检验手机号和验证码
	 * 
	 * @param mobile
	 * @param verifyCode
	 */
	public void verifyCode(String mobile, String verifyCode) {
		mobileCodeService.verifyRegisterCode(mobile, verifyCode);
	}
	
	/**
	 * 
	 * 创建图片
	 * 
	 * @param	image			头像文件
	 * @return 					返回图片id，或者默认图片id如果用户没有传头像。不要返回null
	 * @throws IOException		IO异常
	 * @throws ImageUploadException 创建图片失败时
	 */
	private Integer createImage(MultipartFile image) throws IOException, ImageUploadException {
		if(image == null || image.isEmpty()||image.getBytes().length == 0) {
			return IntConfigKey.DefaultHeadImageId.get();
		}
		
		Integer headImageId = imageService.createImage(WebRequestContext.getUserId(),
				image.getBytes());
		return headImageId;
	}
}
