package com.youshi.zebra.register.service;

import org.springframework.web.multipart.MultipartFile;

import com.youshi.zebra.user.constant.UserType;

/**
 * 
 * Date: May 10, 2016
 * 
 * @author wangsch
 *
 */
public interface RegisterService {
	public Integer registerMobile(String username, UserType userType, String mobile,
			String password, String signature, MultipartFile image, String birthday);
	
}
