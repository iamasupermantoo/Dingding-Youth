package com.youshi.zebra.audio.constants;

import com.dorado.framework.constants.InProduction;
import com.dorado.framework.utils.crypt.DES;
import com.youshi.zebra.core.constants.ZebraConstants;

/**
 * 
 * @author wangsch
 * @date 2017年2月6日
 */
public class AudioConstants {
	public static final DES AUDIO_DES = new DES(InProduction.get() 
			? new byte[] { 10, 19, 2, -62, 38, 16, 45, 9 } : ZebraConstants.DES_KEY_TEST);
	
	public static final String LD_LIBRARY_PATH = "/usr/local/lib/";
	public static final String LAME_BIN_PATH = "/usr/local/bin/lame";
	public static final String FFMPEG_BIN_PATH = "/opt/ffmpeg/bin/ffmpeg";
	public static final String SPEEXENC_BIN_PATH = "/usr/local/bin/speexenc";
//	public static final File UPLOAD_TEMP_DIR = new File("/tmp/shm/");
	
	
	public static final String AUDIO_ACCESS_HOST = InProduction.get() ?
			"au.src.ddlad.com" : "au.z.ziduan.com";

}
