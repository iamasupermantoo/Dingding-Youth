package com.youshi.zebra.recommend.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;
import com.youshi.zebra.core.utils.DateTimeUtils;

/**
 * 首页推荐Feed
 * 
 * @author wangsch
 * @date 2016-09-24
 */
public class RecommendFeedModel extends AbstractModel<Integer> {
	public enum RecommendFeedKeys {
		title,
		content,
		type,
		status,
		
		tags,
		desc,
		image_id,
		referee,
		join_count,
		open_time,
		url,
		pub_time,
		data_type // 0 : course_meta   1: live_meta  2:default
	}
	
	private int authorId;
	
	private int dataId;
	
	private int type;

	public RecommendFeedModel(int id, String data, long createTime, int status ,
			int authorId, int dataId, int type) {
		super(id, data, createTime, status);
		this.authorId = authorId;
		this.dataId = dataId;
		this.type = type;
	}

	public Integer getAuthorId() {
		return authorId;
	}
	
	public int getDataId() {
		return dataId;
	}

	public int getType() {
		return type;
	}
	
	public Integer getReferee() {
		return  ModelUtils.getInt( this,RecommendFeedKeys.referee);
	}
	
	public Integer getDataType() {
		return  ModelUtils.getInt( this,RecommendFeedKeys.data_type);
	}
	
	public String getTitle() {
		return  ModelUtils.getString( this,RecommendFeedKeys.title);
	}
	
	public String getUrl() {
		return  ModelUtils.getString( this,RecommendFeedKeys.url);
	}
	
	public String getPubTime() {
		return DateTimeUtils.getDateTime(getCreateTime());
	}
	
	public Integer getImageId() {
		return ModelUtils.getInt(this, RecommendFeedKeys.image_id);
	}
	
}
