package com.youshi.zebra.view;

import com.youshi.zebra.image.model.ImageModel;
import com.youshi.zebra.image.utils.ImageUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
@ApiModel(value = "切图结构")
public class ImageView {

    protected final ImageModel image;

    public ImageView(ImageModel image) {
        this.image = image;
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
