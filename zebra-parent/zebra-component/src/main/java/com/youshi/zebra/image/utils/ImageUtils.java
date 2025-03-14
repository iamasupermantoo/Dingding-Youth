package com.youshi.zebra.image.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import com.dorado.framework.utils.ApplicationContextHolder;
import com.youshi.zebra.image.constants.ImageConstants;
import com.youshi.zebra.image.model.ImageModel;
import com.youshi.zebra.image.service.ImageService;

/**
 * 
 * @author wangsch
 * @date 2017年1月5日
 */
public class ImageUtils {
	public static List<ImageModel> getImages(List<Integer> imageIds) {
		ImageService service = ApplicationContextHolder.getBean(ImageService.class);
		return service.getListByIds(imageIds);
	}
	
	public static ImageModel getImage(Integer imageId) {
		ImageService service = ApplicationContextHolder.getBean(ImageService.class);
		return service.getById(imageId);
	}
	
    public static final String getFormatByFileName(String fileName) {
    	String format = FilenameUtils.getExtension(fileName);
    	if(support(format)){
    		return format;
    	}
    	throw new IllegalArgumentException("unknown format:" + format);
    }
    
    
    public static final boolean support(String format) {
    	switch (format) {
	        case "webp":
	        case "gif":
	        case "png":
	        case "jpeg":
	        case "jpg":
	        	return true;
	    }
    	return false;
    	
    }
    
    public static final String getFormat(String contentType) {
    	switch (contentType) {
	        case "image/webp":
	            return "webp";
	        case "image/gif":
	            return "gif";
	        case "image/png":
	            return "png";
	        case "image/jpg":
	        	return "jpg";
	        case "image/jpeg":
	        	return "jpeg";
	    }
    	throw new IllegalArgumentException("unknown contentType:" + contentType);
    }
    
    public static final String getMimeType(String format) {
        switch (format) {
            case "webp":
                return "image/webp";
            case "gif":
                return "image/gif";
            case "png":
                return "image/png";
            case "jpg":
            	return "image/jpg";
            case "jpeg":
                return "image/jpeg";
        }
        throw new IllegalArgumentException("unknown format:" + format);
    }

    /**
     * 计算平均亮度 QUES
     * 
     * @param mi
     * @return
     */
    
    /**
     * 
     * @return 图片访问域名
     */
    public static String getHostname() {
        return ImageConstants.IMG_ACCESS_HOST;
    }
    
	/**
     * 
     * @return 阿里云OSS动态切图pattern
     */
	public static String getPattern(String uuid, String format) {
	    return "http://" + getHostname() + "/" + uuid + "." + format
	    		+ "@{w}w_{h}h_75q";
	 }
	
	/**
     * 
     * @return 阿里云OSS动态切图url
     */
	public static String getUrl(String uuid, String format, int w, int h) {
	    return "http://" + getHostname() + "/" + uuid + "." + format
	    		+ "@"+w+"w_"+h+"h_75q";
	 }
	
	
    /**
     * 根据Image实例，获取可访问的url地址
     * 
     * @param images image实例集合
     * @return Map 数字id->url地址，or {@link Collections#emptyMap()}
     */
	public static Map<Integer, String> getImageUrl(Collection<ImageModel> images, int w, int h) {
    	if(CollectionUtils.isEmpty(images)) {
    		return Collections.emptyMap();
    	}
    	Map<Integer, String> urls = new HashMap<>(images.size());
    	for(ImageModel image: images) {
    		String url = getUrl(image.getUuid(), image.getFormat(), w, h);
    		if(StringUtils.isNotEmpty(url)) {
    			urls.put(image.getId(), url);
    		}
    	}
    	
    	return urls;
    }
	
	public static String getImageUrl(ImageModel image, int w, int h) {
		return getImageUrl(Collections.singleton(image), w, h).get(image.getId());
	}
}
