package com.youshi.zebra.view;

import java.util.HashMap;
import java.util.Map;

import com.github.phantomthief.util.ObjectMapperUtils;
import com.youshi.zebra.image.model.ImageModel;
import com.youshi.zebra.image.utils.ImageUtils;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public class ImageView {
	public static ImageView imageView = null;
	static {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("width", 300);
		dataMap.put("height", 200);
		dataMap.put("format", "png");
		
		imageView = new ImageView(new ImageModel(2951, ObjectMapperUtils.toJSON(dataMap), System.currentTimeMillis(), 0));
	}
    protected final ImageModel image;

    public ImageView(ImageModel image) {
        this.image = image;
    }

    public String getURL(Integer w, Integer h) {
    	return ImageUtils.getImageUrl(image, w, h);
    }
    
    protected String getHostname() {
        return ImageUtils.getHostname();
    }
    
    @ApiModelProperty(value = "图片访问pattern", notes="访问图片的url，但是需要对把{w}替换为宽度像素值，{h}替换为高度像素值", required=true)
    public String getPattern() {
      return ImageUtils.getPattern(getId(), image.getFormat());
    }
    
    public String getId() {
        return image.getUuid();
    }

    public Integer getWidth() {
        return image.getWidth();
    }

    public Integer getHeight() {
        return image.getHeight();
    }
}
