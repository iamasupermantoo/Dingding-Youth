package com.youshi.zebra.mobile.dao;

import com.youshi.zebra.mobile.constants.MobileCodeStatus;
import com.youshi.zebra.mobile.model.MobileInfo;

/**
 * 
 * 验证码相关，注册、重置密码等
 * 
 * @author wangsch
 * @date 2016-09-12
 */
public interface MobileInfoDAO {
	/**
	 * 获取注册时的验证码
	 * 
	 * @param phone	手机号
	 * @return	{@link MobileInfo}
	 */
	public MobileInfo getRegisterCode(String phone);
    
	/**
	 * 
	 * 创建注册时的验证码
	 * 
	 * @param phone		手机号
	 * @param code		验证码
	 * @return					主键id
	 */
    public long createRegisterCode(String phone, String code);
    
    /**
     * 更新注册时的验证码，更新状态
     * 
     * @param phone		手机号
     * @param code		新的验证码
     * @param status		更新为什么状态
     * @return					影响行数
     */
    public int updateRegisterCode(String phone, String code, MobileCodeStatus status);
    
    public int updateRegisterCodeStatus(String phone, MobileCodeStatus status);

	/**
	 * 
	 * 
	 * @param phone
	 */
	public int incrRegisterCodeTryCount(String phone);
    
	
	// -------------------------------- 重置密码 ------------------------------------------
    /**
     * 获取重置密码的验证码
     * 
     * @param phone	手机号
     * @return				{@link MobileInfo}
     */
    public MobileInfo getResetCode(String phone);
    
    /**
     * 创建或者更新重置密码的验证码，更新为{@link MobileCodeStatus#MobileCodePending}
     * 
     * @param phone		手机号
     * @param code		验证码
     * @param status		{@link MobileCodeStatus}
     * @return					影响行数
     */
    public int createOrUpdateResetCode(String phone, String code, MobileCodeStatus status);
    
    /**
     * 移除重置密码的验证码
     * 
     * @param phone		手机号
     * @return					影响行数
     */
    public int removeResetCode(String phone);
    
	/**
	 * @param phone
	 * @param status
	 */
	public int updateResetCodeStatus(String phone, MobileCodeStatus status);

	/**
	 * 
	 * 
	 * @param phone
	 */
	public int incrResetCodeTryCount(String phone);
	
	// -------------------------------- 绑定 ------------------------------------------
	/**
	 * @param mobile
	 * @return
	 */
	public MobileInfo getConnectCode(String mobile);

	/**
	 * @param mobile
	 * @param code
	 */
	public int createOrUpdateConnectCode(String phone, String code, MobileCodeStatus status);

	/**
	 * @param mobile
	 */
	public int removeConnectCode(String phone);

	/**
	 * @param phone
	 * @param mobilecodeexpired
	 */
	public int updateConnectCodeStatus(String phone, MobileCodeStatus status);

	/**
	 * @param phone
	 */
	public int incrConnectCodeTryCount(String phone);
}
