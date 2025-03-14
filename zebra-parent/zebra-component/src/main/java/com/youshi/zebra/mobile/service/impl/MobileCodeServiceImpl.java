package com.youshi.zebra.mobile.service.impl;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dorado.framework.constants.InProduction;
import com.dorado.framework.event.utils.PerfUtils;
import com.dorado.framework.utils.DoradoBeanFactory;
import com.ecyrd.speed4j.StopWatch;
import com.youshi.zebra.mobile.constants.MobileCodeStatus;
import com.youshi.zebra.mobile.constants.MobileConstants;
import com.youshi.zebra.mobile.dao.MobileInfoDAO;
import com.youshi.zebra.mobile.exception.MobileAlreadyRegisteredException;
import com.youshi.zebra.mobile.exception.MobileNotRegisteredException;
import com.youshi.zebra.mobile.exception.code.MobileCodeExpiredException;
import com.youshi.zebra.mobile.exception.code.MobileCodeInvalidException;
import com.youshi.zebra.mobile.exception.code.MobileCodeNoneException;
import com.youshi.zebra.mobile.exception.code.MobileCodeWrongException;
import com.youshi.zebra.mobile.model.MobileInfo;
import com.youshi.zebra.mobile.service.MobileCodeService;
import com.youshi.zebra.passport.service.UserPassportService;
import com.youshi.zebra.sms.SmsChannel;
import com.youshi.zebra.sms.SmsException;
import com.youshi.zebra.sms.SmsResult;
import com.youshi.zebra.sms.impl.MobileSmsException;
import com.youshi.zebra.user.constant.UserStatus;
import com.youshi.zebra.user.exception.UserBlockedException;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.service.UserService;

/**
 * 
* 验证码相关，注册+重置密码
* Date: May 11, 2016
* 
 * @author wangsch
 *
 */
@Service
public class MobileCodeServiceImpl implements MobileCodeService {

	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());
	
	/**
	 * 6位验证码
	 */
    private static final int CODE_LENGTH = 6;
    
    /**
     * 最多重试次数
     */
    private static final int MAX_TRY_COUNT = 10;

    @Autowired
    private MobileInfoDAO mobileInfoDAO;

    @Autowired
    private UserService userService;
    
    @Qualifier("lmoblileSmsChannel")
    @Autowired
    private SmsChannel smsChannel;
    
    @Autowired
    private UserPassportService userPassportService;

    @Override
    public String generateRegisterCode(String mobile) {
        MobileInfo mobileInfo = mobileInfoDAO.getRegisterCode(mobile);
        // 手机已经被注册了
        if (mobileInfo != null 
        		&& mobileInfo.getStatus() == MobileCodeStatus.MobileRegistered.getValue()) {
            logger.warn("Mobile already registered, mobile: {}", mobile);
            throw new MobileAlreadyRegisteredException();
        }

        // DB记录
        String code = generateCode(mobile);
        if (mobileInfo == null) {
            mobileInfoDAO.createRegisterCode(mobile, code);
            logger.info("Create mobile info, mobile: {},  code: {}", mobile, code);
        } else {
        	mobileInfoDAO.updateRegisterCode(mobile, code, MobileCodeStatus.MobileCodePending);
            logger.info("Update mobile info to PENDING, mobile: {},  code: {}", mobile, code);
        }
        
        // 消息队列发送验证码
        sendSms(mobile,"用户验证码："+code+"【鼎鼎少年】");
        return code;
    }

    @Override
    public void verifyRegisterCode(String phone, String code) {
        MobileInfo mobileInfo = mobileInfoDAO.getRegisterCode(phone);
        // 各种状态检测
        if (mobileInfo == null) {
        	logger.warn("Mobile code for REGISTER not exist: {}", phone);
            throw new MobileCodeNoneException();
        }
        MobileCodeStatus status = MobileCodeStatus.fromValue(mobileInfo.getStatus());
		if (status == MobileCodeStatus.MobileRegistered) {
            logger.warn("Mobile already registered: {}", phone);
            throw new MobileAlreadyRegisteredException();
        }
		if (status == MobileCodeStatus.MobileCodeInvalid) {
			logger.warn("Mobile code for REGISTER invalid: {}", phone);
			throw new MobileCodeInvalidException();
		}
		if (status == MobileCodeStatus.MobileCodeExpired) {
			logger.warn("Mobile code for REGISTER expired: {}", phone);
			throw new MobileCodeExpiredException();
		}
		
        
        // 过期时间
        long time = mobileInfo.getUpdateTime() != 0 
        		? mobileInfo.getUpdateTime() : mobileInfo.getCreateTime();
		if(System.currentTimeMillis() - time 
        		> TimeUnit.MINUTES.toMillis(mobileInfo.getTtl())) {
        	logger.warn("Mobile register code EXPIRED: {}", phone);
        	mobileInfoDAO.updateRegisterCodeStatus(phone, MobileCodeStatus.MobileCodeExpired);
        	throw new MobileCodeExpiredException();
        }
        
        // 是否相等
        boolean equals = InProduction.get() ? StringUtils.equals(code, mobileInfo.getCode()) : 
			StringUtils.equals(code, MobileConstants.VERIFY_CODE_UNDER_TEST);
		
//		boolean equals = StringUtils.equals(code, mobileInfo.getCode());
        if(!equals) {
        	int tryCount = mobileInfo.getTryCount();
        	// 超过限制
			if(tryCount > MAX_TRY_COUNT) {
        		logger.warn("Mobile register code invalid, mobile: {}, max try count: {}", 
        				phone, MAX_TRY_COUNT);
        		mobileInfoDAO.updateRegisterCodeStatus(phone, MobileCodeStatus.MobileCodeInvalid);
        		throw new MobileCodeInvalidException();
        	}
			
			// 没超过限制
        	mobileInfoDAO.incrRegisterCodeTryCount(phone);
        	
        	logger.warn("Mobile register code is WRONG, mobile: {}, code: {}, input: {}, try: {}", 
        			phone, mobileInfo.getCode(), code, tryCount + 1);
        	throw new MobileCodeWrongException();
        }
        mobileInfoDAO.updateRegisterCodeStatus(phone, MobileCodeStatus.MobileCodeVerified);
        logger.info("Mobile REGISTER code verify OK. mobile: {}", phone);
    }

    @Override
    public void setPhoneStatus(String phone, MobileCodeStatus status) {
        mobileInfoDAO.updateRegisterCodeStatus(phone, status);
    }

    @Override
    public String generateResetCode(String phone) {
        Integer userId = userPassportService.getRegisteredUserId(phone);
        if (userId == null) {
        	logger.warn("Ignore get reset code, mobile NOT REGISTERED: {}", phone);
            throw new MobileNotRegisteredException();
        }
        UserModel user = userService.getById(userId);
        if (UserStatus.isNotNormal(user)) {
            logger.warn("Ignore get reset code, user is BLOCKED, phone: {}, userId: {}", phone, user.getId());
            throw new UserBlockedException();
        }
        
        String code = generateCode(phone);
        mobileInfoDAO.createOrUpdateResetCode(phone, code, MobileCodeStatus.MobileCodePending);
        sendSms(phone, "用户验证码：" + code + "【鼎鼎少年】");
        return code;
    }

    /**
     * 对于重置密码，状态只有{@link MobileCodeStatus#MobileCodePending}，验证成功后直接删除即可。
     * 
     * @param phone
     * @param code
     * 
     */
    @Override
    public void verifyResetCode(String phone, String code) {
        StopWatch stopWatch = PerfUtils.getWatcher("MobileCodeService.verifyResetCode");
        // 各种状态检测
        MobileInfo mobileInfo = mobileInfoDAO.getResetCode(phone);
        if (mobileInfo == null) {
        	logger.warn("Mobile code for RESET not exist: {}", phone);
            throw new MobileCodeNoneException();
        }
        MobileCodeStatus status = MobileCodeStatus.fromValue(mobileInfo.getStatus());
		if (status == MobileCodeStatus.MobileCodeInvalid) {
			logger.warn("Mobile code for RESET invalid: {}", phone);
			throw new MobileCodeInvalidException();
		}
		if (status == MobileCodeStatus.MobileCodeExpired) {
			logger.warn("Mobile code for RESET expired: {}", phone);
			throw new MobileCodeExpiredException();
		}
        
        
        // 过期时间
        long time = mobileInfo.getUpdateTime() != 0 
        		? mobileInfo.getUpdateTime() : mobileInfo.getCreateTime();
		if(System.currentTimeMillis() - time 
        		> TimeUnit.MINUTES.toMillis(mobileInfo.getTtl())) {
        	logger.warn("Mobile RESET code EXPIRED: {}", phone);
        	mobileInfoDAO.updateResetCodeStatus(phone, MobileCodeStatus.MobileCodeExpired);
        	throw new MobileCodeExpiredException();
        }
        
        // 是否相等
        boolean equals = InProduction.get() ? StringUtils.equals(code, mobileInfo.getCode()) : 
			StringUtils.equals(code, MobileConstants.VERIFY_CODE_UNDER_TEST);
		
//		boolean equals = StringUtils.equals(code, mobileInfo.getCode());
        if(!equals) {
        	int tryCount = mobileInfo.getTryCount();
        	// 超过限制
			if(tryCount > MAX_TRY_COUNT) {
        		logger.warn("Mobile RESET code invalid, mobile: {}, max try count: {}", 
        				phone, MAX_TRY_COUNT);
        		mobileInfoDAO.updateResetCodeStatus(phone, MobileCodeStatus.MobileCodeInvalid);
        		throw new MobileCodeInvalidException();
        	}
			
			// 没超过限制
        	mobileInfoDAO.incrResetCodeTryCount(phone);
        	
        	logger.warn("Mobile RESET code is WRONG, mobile: {}, code: {}, input: {}, try: {}", 
        			phone, mobileInfo.getCode(), code, tryCount + 1);
        	throw new MobileCodeWrongException();
        }
        mobileInfoDAO.removeResetCode(phone);
        logger.info("Mobile RESET code verify OK. mobile: {}", phone);
        stopWatch.stop();
    }

    @Override
    public void removeResetCode(String mobile) {
        mobileInfoDAO.removeResetCode(mobile);
    }
    
    @Override
    public String generateConnectCode(String mobile) {
    	// TODO 校验
    	
        // DB记录
        String code = generateCode(mobile);
        mobileInfoDAO.createOrUpdateConnectCode(mobile, code, MobileCodeStatus.MobileCodePending);
        
        sendSms(mobile,"用户验证码："+code+"【鼎鼎少年】");
        return code;
    }

	/* (non-Javadoc)
	 * @see com.youshi.zebra.mobile.service.MobileCodeService#verifyConnectCode(java.lang.String, java.lang.String)
	 */
	@Override
	public void verifyConnectCode(String phone, String code) {

        StopWatch stopWatch = PerfUtils.getWatcher("MobileCodeService.verifyConnectCode");
        // 各种状态检测
        MobileInfo mobileInfo = mobileInfoDAO.getConnectCode(phone);
        if (mobileInfo == null) {
        	logger.warn("Mobile code for CONNECT not exist: {}", phone);
            throw new MobileCodeNoneException();
        }
        MobileCodeStatus status = MobileCodeStatus.fromValue(mobileInfo.getStatus());
		if (status == MobileCodeStatus.MobileCodeInvalid) {
			logger.warn("Mobile code for CONNECT invalid: {}", phone);
			throw new MobileCodeInvalidException();
		}
		if (status == MobileCodeStatus.MobileCodeExpired) {
			logger.warn("Mobile code for CONNECT expired: {}", phone);
			throw new MobileCodeExpiredException();
		}
        
        
        // 过期时间
        long time = mobileInfo.getUpdateTime() != 0 
        		? mobileInfo.getUpdateTime() : mobileInfo.getCreateTime();
		if(System.currentTimeMillis() - time 
        		> TimeUnit.MINUTES.toMillis(mobileInfo.getTtl())) {
        	logger.warn("Mobile RESET code EXPIRED: {}", phone);
        	mobileInfoDAO.updateConnectCodeStatus(phone, MobileCodeStatus.MobileCodeExpired);
        	throw new MobileCodeExpiredException();
        }
        
        // 是否相等
        boolean equals = InProduction.get() ? StringUtils.equals(code, mobileInfo.getCode()) : 
			StringUtils.equals(code, MobileConstants.VERIFY_CODE_UNDER_TEST);
//        boolean equals = StringUtils.equals(code, mobileInfo.getCode());
        if(!equals) {
        	int tryCount = mobileInfo.getTryCount();
        	// 超过限制
			if(tryCount > MAX_TRY_COUNT) {
        		logger.warn("Mobile RESET code invalid, mobile: {}, max try count: {}", 
        				phone, MAX_TRY_COUNT);
        		mobileInfoDAO.updateConnectCodeStatus(phone, MobileCodeStatus.MobileCodeInvalid);
        		throw new MobileCodeInvalidException();
        	}
			
			// 没超过限制
        	mobileInfoDAO.incrConnectCodeTryCount(phone);
        	
        	logger.warn("Mobile CONNECT code is WRONG, mobile: {}, code: {}, input: {}, try: {}", 
        			phone, mobileInfo.getCode(), code, tryCount + 1);
        	throw new MobileCodeWrongException();
        }
        mobileInfoDAO.removeConnectCode(phone);
        logger.info("Mobile CONNECT code verify OK. mobile: {}", phone);
        stopWatch.stop();
		
	}
    
    public void removeConnectCode(String mobile) {
        mobileInfoDAO.removeConnectCode(mobile);
    }
    
    
    // --------------------------------------- private methods --------------------------------------
    private String generateCode(String phone) {
        return RandomStringUtils.randomNumeric(CODE_LENGTH);
    }
    
    private void sendSms(String mobile, String message) {
    	if(!InProduction.get()) {
    		logger.info("Ignore sms to phone:{}, msg:{}", mobile, message);
    		return;
    	}
        
        try {
        	SmsResult sendSms = smsChannel.sendSms(mobile, message);
        	logger.info("send sms:{},{},{}", message, mobile,sendSms.getMsgId());
        } catch (SmsException e) {
        	if (e instanceof MobileSmsException) {
        		MobileSmsException ise = (MobileSmsException) e;
        		logger.info("send sms submit /! state:" + ise.getState() + ",msgState:"
        				+ ise.getMsgState() + ",mobile:" + mobile, e);
        	} else {
        		logger.warn("send sms submit fail! mobile:" + mobile, e);
        	}
        }
    }
    
    public static void main(String[] args) throws SmsException {
    	SmsChannel smsChannel = DoradoBeanFactory.getBean(SmsChannel.class);
		
		String mobile = "15712889750";
		String code = "2010";
		SmsResult sendSms = smsChannel.sendSms(mobile, "用户验证码："+code+"【鼎鼎少年】");
		System.out.println(sendSms);
	}
}
