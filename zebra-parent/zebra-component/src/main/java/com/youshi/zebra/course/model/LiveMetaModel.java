package com.youshi.zebra.course.model;

import java.util.Date;
import java.util.List;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;
import com.youshi.zebra.course.constants.CourseLevel;

/**
 * 
 * @author codegen
 */
public class LiveMetaModel extends AbstractModel<Integer>{
	public enum LiveMetaKeys {
		// db 字段
		type,
		open_time,
		teacher_id,
	
		// data字段
		
		status,

		name,
		
		/** 描述 */
		desc,
		
		/** 图片id */
		image_id,
		
		big_image_id,
				
		/** 
		 * {@link CourseLevel}
		 */
		level,
		
		/** 价格 */
		price,
		
		sub_notes,
		suitable_crowds,
		
		join_count,
		
		real_join_count,
		
		share_brief,   		//微信分享时的描述
		share_jump_url ,	//	微信分享，跳转链接
		share_image_id,		//微信分享，图片id
		
		
	}
	
	private int type;
	private String openTime;
	private Integer teacherId;
	
	public LiveMetaModel(
			int id, String data, long createTime, int status,
			int type,
			String openTime,
			Integer teacherId
			) {
		super(id, data, createTime, status);
		this.type = type;
		this.openTime = openTime;
		this.setTeacherId(teacherId);
	}

	public int getType() {
		return type;
	}
	public String getOpenTime() {
		return openTime;
	}
	
	public String getName() {
		return ModelUtils.getString(this, LiveMetaKeys.name);
	}
	
	public String getDesc() {
		return ModelUtils.getString(this, LiveMetaKeys.desc);
	}
	
//	public String getBrief() {
//		return ModelUtils.getString(this, LiveMetaKeys.brief);
//	}
	
	public Integer getImageId() {
		return ModelUtils.getInt(this, LiveMetaKeys.image_id);
	}
	
	public Integer getBigImageId() {
		return ModelUtils.getInt(this, LiveMetaKeys.big_image_id);
	}
	
	
	public Integer getLevel() {
		return ModelUtils.getInt(this, LiveMetaKeys.level);
	}
	
	public String getPrice() {
		return ModelUtils.getString(this, LiveMetaKeys.price);
	}
	
	public List<String> getSubNotes() {
		return ModelUtils.getList(this, LiveMetaKeys.sub_notes, String.class);
	}
	
	public String getSuitableCrowds() {
		return ModelUtils.getString(this, LiveMetaKeys.suitable_crowds);
	}
	
	public Integer getJoinCount() {
		return ModelUtils.getInt(this, LiveMetaKeys.join_count);
	}
	
	public String getShareBrief() {
		return ModelUtils.getString(this, LiveMetaKeys.share_brief);
	}
	
	public Integer getShareImageId() {
		return ModelUtils.getInt(this, LiveMetaKeys.share_image_id);
	}
	
	public String getShareJumpUrl() {
		return ModelUtils.getString(this, LiveMetaKeys.share_jump_url);
	}

	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}
	
	
}
