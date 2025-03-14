package com.youshi.zebra.course.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;

/**
 * 
 * @author codegen
 */
public class TryCourseModel extends AbstractModel<Integer>{
	public enum TryCourseKeys {
		// db 字段
		cm_id,
		course_id,
		student_id,
		chat_result,
		paid,
		planned,
		update_time,
	
		// data字段
		remark,
	}
	
	private int cmId;
	private int courseId;
	private int studentId;
	private int chatResult;
	private boolean paid;
	private boolean planned;
	private long updateTime;
	
	public TryCourseModel(
			int id, String data, long createTime, int status,
			int cmId,
			int courseId,
			int studentId,
			int chatResult,
			boolean paid,
			boolean planned,
			long updateTime
			) {
		super(id, data, createTime, status);
		this.cmId = cmId;
		this.courseId = courseId;
		this.studentId = studentId;
		this.chatResult = chatResult;
		this.paid = paid;
		this.planned = planned;
		this.updateTime = updateTime;
	}

	public int getCmId() {
		return cmId;
	}
	public int getCourseId() {
		return courseId;
	}
	public int getStudentId() {
		return studentId;
	}
	public int getChatResult() {
		return chatResult;
	}
	public boolean getPaid() {
		return paid;
	}
	public boolean getPlanned() {
		return planned;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	
	public String getRemark() {
		return ModelUtils.getString(this, TryCourseKeys.remark);
	}
	
}
