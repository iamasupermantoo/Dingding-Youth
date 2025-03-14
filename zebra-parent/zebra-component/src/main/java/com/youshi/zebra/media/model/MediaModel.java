package com.youshi.zebra.media.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;

/**
 * 
 * 资源库中每一条代表一个媒体资源。如：图片、音频、视频
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
public class MediaModel extends AbstractModel<Integer>{
	public enum MediaKeys {
		type,
		// data字段
		name, 
		desc,
		image_id,
		video_id,
		audio_id, 
	}
	
	private int type;
	
	public MediaModel(Integer id, String data, long createTime, int status,
			int type) {
		super(id, data, createTime, status);
		this.type = type;
		
	}
	
	public String getName() {
		return ModelUtils.getString(this, MediaKeys.name);
	}
	
	public String getDesc() {
		return ModelUtils.getString(this, MediaKeys.desc);
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public Integer getImageId() {
		return ModelUtils.getInt(this, MediaKeys.image_id);
	}
	
	public Integer getAudioId() {
		return ModelUtils.getInt(this, MediaKeys.audio_id);
	}
	
	public Integer getVideoId() {
		return ModelUtils.getInt(this, MediaKeys.video_id);
	}
}
