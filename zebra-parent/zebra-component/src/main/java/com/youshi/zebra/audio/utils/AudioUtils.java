package com.youshi.zebra.audio.utils;

import java.util.List;

import com.dorado.framework.utils.ApplicationContextHolder;
import com.youshi.zebra.audio.AudioService;
import com.youshi.zebra.audio.constants.AudioConstants;
import com.youshi.zebra.audio.model.AudioModel;

/**
 * 
 * @author wangsch
 * @date 2017年2月6日
 */
public class AudioUtils {
	
	public static List<AudioModel> getAudios(List<Integer> audioIds) {
		AudioService service = ApplicationContextHolder.getBean(AudioService.class);
		return service.getListByIds(audioIds);
	}
	
	public static AudioModel getAudio(Integer audioId) {
		AudioService service = ApplicationContextHolder.getBean(AudioService.class);
		return service.getById(audioId);
	}
	
	/**
	 * 
	 * @return 音频访问域名
	 */
	public static String getHostname() {
		return AudioConstants.AUDIO_ACCESS_HOST;
	}
	
	public static String getUrl(String uuid, String format) {	
		return "http://" + getHostname() + "/" + uuid + "." + format;
	}
	
	
	public static String getUrl(AudioModel audio) {
		return "http://" + getHostname() + "/" + audio.getUuid() + "." + audio.getFormat();
	}

}
