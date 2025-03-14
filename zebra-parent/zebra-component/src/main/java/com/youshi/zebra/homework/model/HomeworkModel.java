package com.youshi.zebra.homework.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;
import com.youshi.zebra.lesson.model.LessonHistModel;
import com.youshi.zebra.lesson.model.LessonPlanModel;

/**
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
public class HomeworkModel extends AbstractModel<Integer>{
	public enum HomeworkKeys {
		// db字段
		teacher_id,// TODO
		student_id,
		lesson_id,
		course_id,
		status,
		
		// data字段
		date,
		time,
		
		title,
		content,
		
		// 便于查询的冗余字段
		teacher,
		student,
		
		intime,
		complete,
		quality,
		remark,
	}
	
	private int studentId;
	
	private int teacherId;
	
	private int courseId;
	
	/**
	 * lesson的id，来自{@link LessonHistModel}或者{@link LessonPlanModel}的id
	 */
	private int lessonId;
	
	
	public HomeworkModel(int id, String data, long createTime, int status,
			int studentId, int teacherId, int courseId, int lessonId) {
		super(id, data, createTime, status);
		this.studentId = studentId;
		this.teacherId = teacherId;
		this.courseId = courseId;
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
	
	public int getCourseId() {
		return courseId;
	}
	
	public int getLessonId() {
		return lessonId;
	}


	public void setLessonId(int lessonId) {
		this.lessonId = lessonId;
	}

	public String getTitle() {
		return ModelUtils.getString(this, HomeworkKeys.title);
	}
	
	public String getContent() {
		return ModelUtils.getString(this, HomeworkKeys.content);
	}


	public String getDate() {
		return ModelUtils.getString(this, HomeworkKeys.date);
	}

	public String getTime() {
		return ModelUtils.getString(this, HomeworkKeys.time);
	}
	
//	public String getTeacher() {
//		return ModelUtils.getString(this, HomeworkKeys.teacher);
//	}
	
//	public String getStudent() {
//		return ModelUtils.getString(this, HomeworkKeys.student);
//	}
	
	public Boolean getIntime() {
		return ModelUtils.getBoolean(this, HomeworkKeys.intime, false);
	}
	
	public Integer getComplete() {
		return ModelUtils.getInt(this, HomeworkKeys.complete);
	}
	
	public Integer getQuality() {
		return ModelUtils.getInt(this, HomeworkKeys.quality);
	}
	
	public String getRemark() {
		return ModelUtils.getString(this, HomeworkKeys.remark);
	}
}
