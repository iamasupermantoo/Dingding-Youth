/**
 * 
 */
package com.youshi.zebra.tech;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.dorado.framework.utils.crypt.DES;
import com.youshi.zebra.image.constants.ImageConstants;
import com.youshi.zebra.image.model.ImageModel;
import com.youshi.zebra.user.constant.UserConstants;
import com.youshi.zebra.user.model.UserModel;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年1月9日
 */
@Component
public class ZebraTechInitBean {
	private Logger logger = LoggerFactory.getLogger(ZebraTechInitBean.class);
	
	@PostConstruct
	public void init() {
		Map<Class<?>, DES> desMap = new HashMap<>();
		desMap.put(ImageModel.class, ImageConstants.IMAGE_DES);
		desMap.put(UserModel.class, UserConstants.USER_DES);
		
		UuidUtils.init(desMap);
		
		logger.info("Zebra api init OK");
	}
	
}
