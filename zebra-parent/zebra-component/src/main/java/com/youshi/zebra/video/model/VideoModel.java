package com.youshi.zebra.video.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;

/**
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
public class VideoModel extends AbstractModel<Integer>{
	public enum VideoKeys {
		format,
	}
	
	
	public VideoModel(Integer id, String data, long createTime, int status) {
		super(id, data, createTime, status);
	}
	
	public String getFormat() {
		return ModelUtils.getString(this, VideoKeys.format);
	}
	
}
