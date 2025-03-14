package com.youshi.zebra.wx.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.youshi.zebra.image.model.ImageModel;
import com.youshi.zebra.image.utils.ImageUtils;

import io.swagger.annotations.ApiModel;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
@ApiModel("图片切图")
public class ImageView {
	
    protected final ImageModel image;

    public ImageView(ImageModel image) {
        this.image = image;
    }

    protected String getHostname() {
        return ImageUtils.getHostname();
    }
    
    public String getPattern() {
      return ImageUtils.getPattern(getId(), getFormat());
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
    
    @JsonIgnore
    public String getFormat() {
        return image.getFormat();
    }
}
