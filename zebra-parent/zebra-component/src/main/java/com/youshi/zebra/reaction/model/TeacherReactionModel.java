package com.youshi.zebra.reaction.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;

/**
 * 
 * @author wangsch
 * @date 2017年2月23日
 */
public class TeacherReactionModel extends AbstractModel<Integer>{
	public enum StudentReactionKeys {
		// 字段
		course_id,
		lesson_id,
		student_id,
		teacher_id,
		status,
		
		// data字段
		/** 课堂表现 */
		behavior,
		
		/** 教学理念 */
		idea,
		
		remark,
		
		star,
		
		student,
		course,
		cnt,
		date,
		time,
	}
	
	private int courseId;
	private int lessonId;
	private int studentId;
	private int teacherId;
	
	public TeacherReactionModel(Integer id, String data, long createTime, int status, 
			int courseId, int lessonId, int studentId, int teacherId) {
		super(id, data, createTime, status);
		this.courseId = courseId;
		this.lessonId = lessonId;
		this.studentId = studentId;
		this.teacherId = teacherId;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getLessonId() {
		return lessonId;
	}

	public void setLessonId(int lessonId) {
		this.lessonId = lessonId;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}
	
	public Integer getStar() {
		return ModelUtils.getInt(this, StudentReactionKeys.star);
	}
	
	public Integer getIdea() {
		return ModelUtils.getInt(this, StudentReactionKeys.idea);
	}
	
	public Integer getBehavior() {
		return ModelUtils.getInt(this, StudentReactionKeys.behavior);
	}
	
	public String getRemark() {
		return ModelUtils.getString(this, StudentReactionKeys.remark);
	}
	
	public String getStudent() {
		return ModelUtils.getString(this, StudentReactionKeys.student);
	}
	
	public String getCourse() {
		return ModelUtils.getString(this, StudentReactionKeys.course);
	}
	
	public Integer getCnt() {
		return ModelUtils.getInt(this, StudentReactionKeys.cnt);
	}
	
	public String getDate() {
		return ModelUtils.getString(this, StudentReactionKeys.date);
	}
	
	public String getTime() {
		return ModelUtils.getString(this, StudentReactionKeys.time);
	}
}
