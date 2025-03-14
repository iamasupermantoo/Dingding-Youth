package com.youshi.zebra.course.model;

import java.util.List;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;
import com.youshi.zebra.course.constants.CourseLevel;



/**
 * 课程元信息。<br />
 * 
 * Notes: {@link CourseMetaModel}和{@link CourseModel}的区别
 * 
 * {@link CourseMetaModel}课程元信息：用于课程展示，提供一些基础信息，如：老师、课程描述、费用等，供学生挑选。数据相对比较稳定，来源于运营后台的录入。
 * 
 * {@link CourseModel}课程信息：表示一个处于教学周期中的一个课程，当一个学生选好课，交完费后，生成一个{@link CourseModel}实体，
 * 将“课程元信息id”、学生id、老师id、助教id等信息想关联。之后的教学过程，如：上课、留作业写作业、评价等都是在这一个课程的范围内进行的。
 * 
 * @author wangsch
 * @date 2017年2月7日
 * 
 * @see CourseModel
 * 
 */
public class CourseMetaModel extends AbstractModel<Integer>{
	public enum CourseMetaKeys {
		/** 教材id */
		book_id,
		
		status,
		
		// data字段
		name,
		
		/** 描述 */
		desc,
		
		/** 图片id */
		image_id,
		
		big_image_id,
		
		/** 总课时 */
		total_cnt,
		
		/** 
		 * {@link CourseLevel}
		 */
		level,
		
		type,
		
		/** 价格 */
		price,
		
		sub_notes,
		suitable_crowds,
		join_count,
		
		share_brief,   		// 微信分享时的描述
		share_jump_url ,	// 微信分享，跳转链接
		share_image_id,		// 微信分享，图片id
	}

	private int bookId;
	
	private int type;
	
	
	public CourseMetaModel(
			int id, String data, long createTime, int status,
			int bookId, int type) {
		super(id, data, createTime, status);
		this.bookId = bookId;
		this.type = type;
	}
	
	public int getBookId() {
		return bookId;
	}
	
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public String getName() {
		return ModelUtils.getString(this, CourseMetaKeys.name);
	}
	
	public String getDesc() {
		return ModelUtils.getString(this, CourseMetaKeys.desc);
	}
	
//	public String getBrief() {
//		return ModelUtils.getString(this, CourseMetaKeys.brief);
//	}
	
	public Integer getImageId() {
		return ModelUtils.getInt(this, CourseMetaKeys.image_id);
	}
	
	public Integer getBigImageId() {
		return ModelUtils.getInt(this, CourseMetaKeys.big_image_id);
	}
	
	public Integer getTotalCnt() {
		return ModelUtils.getInt(this, CourseMetaKeys.total_cnt);
	}
	
	public Integer getLevel() {
		return ModelUtils.getInt(this, CourseMetaKeys.level);
	}
	
	public String getPrice() {
		return ModelUtils.getString(this, CourseMetaKeys.price);
	}
	
	public List<String> getSubNotes() {
		return ModelUtils.getList(this, CourseMetaKeys.sub_notes, String.class);
	}
	
	public String getSuitableCrowds() {
		return ModelUtils.getString(this, CourseMetaKeys.suitable_crowds);
	}
	
	public Integer getJoinCount() {
		return ModelUtils.getInt(this, CourseMetaKeys.join_count);
	}
	
	public String getShareBrief() {
		return ModelUtils.getString(this, CourseMetaKeys.share_brief);
	}
	
	public Integer getShareImageId() {
		return ModelUtils.getInt(this, CourseMetaKeys.share_image_id);
	}
	
	public String getShareJumpUrl() {
		return ModelUtils.getString(this, CourseMetaKeys.share_jump_url);
	}
}
