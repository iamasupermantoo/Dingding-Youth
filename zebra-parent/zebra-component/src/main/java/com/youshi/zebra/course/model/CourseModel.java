package com.youshi.zebra.course.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;

/**
 * 
 * 课程信息<br />
 * 
 * 请参考{@link CourseMetaModel}
 * 
 * @author wangsch
 * @date 2017年2月7日
 * 
 * @see CourseMetaModel
 * 
 */
public class CourseModel extends AbstractModel<Integer>{
	public enum CourseKeys {
		id,
		cm_id,
		book_id,
		student_id,
		status,
		type,
		
		// data字段
		/** 课程名 */
		course,
		student,
		total_cnt,
		image_id
	}
	
	private int cmId;
	
	private int studentId;
	
	private int bookId;
	
	private int type;
	
	public CourseModel(
			int id, String data, long createTime, int status,
			int cmId, int studentId, int bookId, int type) {
		super(id, data, createTime, status);
		this.cmId = cmId;
		this.studentId = studentId;
		this.bookId = bookId;
		this.type = type;
	}

	public int getCmId() {
		return cmId;
	}
	
	public void setCmId(int cmId) {
		this.cmId = cmId;
	}
	
	public int getStudentId() {
		return studentId;
	}
	
	public void setStudentId(int studentId) {
		this.studentId = studentId;
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
	
	public String getStudent() {
		return ModelUtils.getString(this, CourseKeys.student);
	}
	
	public String getCourse() {
		return ModelUtils.getString(this, CourseKeys.course);
	}
	
	public Integer getImageId() {
		return ModelUtils.getInt(this, CourseKeys.image_id);
	}
	
	public Integer getTotalCnt() {
		return ModelUtils.getInt(this, CourseKeys.total_cnt);
	}
	
}
