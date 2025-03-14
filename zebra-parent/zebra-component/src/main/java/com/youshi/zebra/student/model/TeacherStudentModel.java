package com.youshi.zebra.student.model;

import com.dorado.framework.crud.model.impl.AbstractModel;

/**
 * 
 * @author wangsch
 * @date 2017年2月21日
 */
public class TeacherStudentModel extends AbstractModel<Integer>{
	public enum TeacherStudentKeys {
		
		
		
	}
	
	private int teacherId;
	private int studentId;
	private long createTime;

	public TeacherStudentModel(Integer id, String data, long createTime, int status, 
			int teacherId, int studentId) {
		super(id, data, createTime, status);
		this.teacherId = teacherId;
		this.studentId = studentId;
	}

	public int getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	
	
	
}
