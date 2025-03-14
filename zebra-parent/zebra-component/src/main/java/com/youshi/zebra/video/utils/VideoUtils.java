package com.youshi.zebra.video.utils;

import com.youshi.zebra.video.constants.VideoConstants;
import com.youshi.zebra.video.model.VideoModel;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
public class VideoUtils {
	
	/**
	 * 
	 * @return 视频访问域名
	 */
	public static String getHostname() {
		return VideoConstants.VIDEO_ACCESS_HOST;
	}
	
	public static String getUrl(String uuid, String format) {	
		return "http://" + getHostname() + "/" + uuid + "." + format;
	}
	
	
	public static String getUrl(VideoModel video) {
		return "http://" + getHostname() + "/" + video.getUuid() + "." + video.getFormat();
	}

}
