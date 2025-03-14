package com.youshi.zebra.image.constants;

import com.dorado.framework.constants.InProduction;

/**
 * 
 * 奶奶个爪，请使用RAM子账号操作OSS
 * 
 * @author wangsch
 * @date 2016-09-12
 * 
 */
public interface AliyunOssConstants {
    /**
     * 阿里云OSS endpoint
     */
	public final static String OSS_ENDPOINT = InProduction.get() ? "http://oss-cn-beijing.aliyuncs.com" 
		: "http://oss-cn-beijing.aliyuncs.com";
	
	/**
     *
     * 阿里云OSS Bucket名
     */
   public final static String BUCKET_NAME = InProduction.get() ? "ddlad-img" : "zebra-image-dev";
   
   
   public final static String AUDIO_BUCKET_NAME = InProduction.get() ? "ddlad-au" : "zebra-audio-dev";
   
   public final static String VIDEO_BUCKET_NAME = InProduction.get() ? "ddlad-vi" : "zebra-video-dev";
   
   
}
