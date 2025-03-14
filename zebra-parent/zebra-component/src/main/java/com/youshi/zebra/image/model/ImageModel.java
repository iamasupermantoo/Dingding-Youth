package com.youshi.zebra.image.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;
import com.dorado.framework.utils.crypt.DES;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.youshi.zebra.image.constants.ImageConstants;

/**
 * 
 * @author wangsch
 * @date 2017年1月4日
 */
public class ImageModel extends AbstractModel<Integer> {

    public enum ImageKeys {
        width,
        height,
        format,
        quality,
        size,
        author,
    }
    
	public ImageModel() {
		super();
	}

	public ImageModel(Integer id, String data, long createTime, int status) {
		super(id, data, createTime, status);
	}

	@JsonIgnore
	public Integer getWidth() {
        return ModelUtils.getInt(this, ImageKeys.width.name());
    }

	@JsonIgnore
    public Integer getHeight() {
        return ModelUtils.getInt(this, ImageKeys.height);
    }

	@JsonIgnore
    public String getFormat() {
        return ModelUtils.getString(this, ImageKeys.format);
    }

	@JsonIgnore
    public Integer getQuality() {
        return ModelUtils.getInt(this, ImageKeys.quality, 0);
    }

	@JsonIgnore
    public long getSize() {
        return ModelUtils.getLong(this, ImageKeys.size, 0);
    }

	@Override
	public DES des() {
		return ImageConstants.IMAGE_DES;
	}
}
