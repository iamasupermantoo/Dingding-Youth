package com.youshi.zebra.live.utils;

import com.youshi.zebra.video.constants.VideoConstants;

/**
 * 
 * @author wangsch
 * @date 2017年6月26日
 */
public class LiveUtils {
//	public String getReplayUrl(Integer courseId, Integer lessonId) {
//		String key = LiveService.videoOssKey(courseId, lessonId);
//		return getUrl(key);
//	}
	
	public static String getHostname() {
		return VideoConstants.VIDEO_ACCESS_HOST;
	}
	
	public static String getReplayUrl(String key) {	
		return "http://" + getHostname() + "/" + key;
	}
}
