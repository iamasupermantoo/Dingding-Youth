package com.youshi.zebra.mobile.service;

import com.youshi.zebra.mobile.constants.MobileCodeStatus;

/**
 * 
 * 手机验证码Service
 * 
 * Date: May 17, 2016
 * @author wangsch
 *
 */
public interface MobileCodeService {

    /**
     * 生成注册用的验证码
     * 
     * @param phone	手机号
     * @return 验证码
     */
    String generateRegisterCode(String phone);
    
    void verifyRegisterCode(String phone, String code);

    void setPhoneStatus(String phone, MobileCodeStatus status);

    // ------------------------------------------------------- 重置密码 -------------------------------------------------------------
    String generateResetCode(String phone);

    void verifyResetCode(String phone, String code);

    void removeResetCode(String mobile);
    
    // ------------------------------------------------------- 绑定 -----------------------------------------------------------------
    String generateConnectCode(String phone);

	/**
	 * @param mobile
	 * @param code
	 */
	void verifyConnectCode(String mobile, String code);
    
    
    
}
