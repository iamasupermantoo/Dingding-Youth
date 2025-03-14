package com.youshi.zebra.mine.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.utils.DoradoBeanFactory;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.model.UserModel.StudentKey;
import com.youshi.zebra.user.model.UserModel.UserKey;
import com.youshi.zebra.user.service.UserService;

/**
 * 
 * “我的”Service
 * 
 * @author wangsch
 * @date 2017年2月9日
 */
@Service
public class MineService {
	
	@Autowired
	private UserService userService;
	
	
	public void updateInfo(Integer userId, Integer headImgId, String name, String province, String region, Integer gender,
			String signature, String grade, String birthday) {
		UserModel user = userService.getById(userId);
		
		Map<String, Object> dataMap = DoradoMapperUtils.fromJSON(user.getData(), 
				HashMap.class, String.class, Object.class);
		if(headImgId != null) {
			dataMap.put(UserKey.head_image_id.name(), headImgId);
		}
		dataMap.put(UserKey.province.name(), province);
		dataMap.put(UserKey.region.name(), region);
		dataMap.put(UserKey.gender.name(), gender);
		dataMap.put(StudentKey.grade.name(), grade);
		if(StringUtils.isNotEmpty(birthday)) {
			dataMap.put(UserKey.birthday.name(), birthday);
		}
		
		String data = DoradoMapperUtils.toJSON(dataMap);
		userService.update(userId, name, signature, data);
		
	}
}
