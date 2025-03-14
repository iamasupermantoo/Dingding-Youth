package com.youshi.zebra.live.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youshi.zebra.live.model.LiveInfo;

/**
 * 
 * @author wangsch
 * @date 2017年3月15日
 */
@Service
public class LiveAdminService {
	
	@Autowired
	private LiveService liveService;
	
	public void markFail(Integer adminId, Integer courseId, Integer lessonId) {
		
	}
	

	
	public LiveInfo getAudienceLiveInfo(Integer userId, Integer courseId, Integer lessonId) {
		// TODO
		return null;
	}
	
}
