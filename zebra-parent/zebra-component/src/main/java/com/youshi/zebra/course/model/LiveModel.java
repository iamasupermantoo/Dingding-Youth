package com.youshi.zebra.course.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;

/**
 * 
 * @author codegen
 */
public class LiveModel extends AbstractModel<Integer>{
	public enum LiveKeys {
		// db 字段
		lm_id,
		student_id,
		type,
		open_time,
	
		// data字段
		status,
		course,
		student,
		total_cnt,
		image_id,
		join_count
	}
	
	private int lmId;
	private int studentId;
	private int type;
	private String openTime;
	
	public LiveModel(
			int id, String data, long createTime, int status,
			int lmId,
			int studentId,
			int type,
			String openTime
			) {
		super(id, data, createTime, status);
		this.lmId = lmId;
		this.studentId = studentId;
		this.type = type;
		this.openTime = openTime;
	}

	public int getLmId() {
		return lmId;
	}
	public int getStudentId() {
		return studentId;
	}
	public int getType() {
		return type;
	}
	public String getOpenTime() {
		return openTime;
	}
	
	
	public String getStudent() {
		return ModelUtils.getString(this, LiveKeys.student);
	}
	
	public String getCourse() {
		return ModelUtils.getString(this, LiveKeys.course);
	}
	
	public Integer getImageId() {
		return ModelUtils.getInt(this, LiveKeys.image_id);
	}
	
	public Integer getTotalCnt() {
		return ModelUtils.getInt(this, LiveKeys.total_cnt);
	}
	
	public Integer getJoinCount() {
		return ModelUtils.getInt(this, LiveKeys.join_count);
	}
	
}
