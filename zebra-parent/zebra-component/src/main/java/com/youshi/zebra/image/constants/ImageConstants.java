package com.youshi.zebra.image.constants;

import com.dorado.framework.constants.InProduction;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.dorado.framework.utils.crypt.DES;
import com.google.common.collect.ImmutableMap;
import com.youshi.zebra.core.constants.ZebraConstants;
import com.youshi.zebra.image.model.ImageModel;
import com.youshi.zebra.image.model.ImageModel.ImageKeys;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public class ImageConstants {
	// 测试用
	public static final ImageModel C_IMAGE;
	static {
		ImmutableMap<ImageKeys, Object> map = ImmutableMap.<ImageKeys, Object>builder()
				.put(ImageKeys.author, 1)
				.put(ImageKeys.width, 200)
				.put(ImageKeys.height, 200)
				.put(ImageKeys.format, "png")
				.build();
	    String data = DoradoMapperUtils.toJSON(map);
	    long time = System.currentTimeMillis();
	    
	    C_IMAGE = new ImageModel(1, data, time, 1);
	}
	
	
	public static final DES IMAGE_DES = new DES(InProduction.get() 
			? new byte[] { 113, 39, 43, 38, 33, 55, -10, 72 } : ZebraConstants.DES_KEY_TEST);
	
	public static final String IMG_ACCESS_HOST = InProduction.get() ?
			"img.src.ddlad.com" : "img.z.ziduan.com";
}
