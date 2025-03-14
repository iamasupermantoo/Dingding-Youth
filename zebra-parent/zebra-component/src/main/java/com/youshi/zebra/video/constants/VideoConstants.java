package com.youshi.zebra.video.constants;

import com.dorado.framework.constants.InProduction;
import com.dorado.framework.utils.crypt.DES;
import com.youshi.zebra.core.constants.ZebraConstants;

/**
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
public class VideoConstants {
	
	public static final DES VIDEO_DES = new DES(InProduction.get() 
			? new byte[] { 10, 9, 2, 22, 38, 13, 45, 9 } : ZebraConstants.DES_KEY_TEST);
	
	public static final String VIDEO_ACCESS_HOST = InProduction.get() ?
			"vi.src.ddlad.com" : "vi.z.ziduan.com";
	
	
}
