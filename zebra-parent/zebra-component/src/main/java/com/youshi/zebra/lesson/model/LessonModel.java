package com.youshi.zebra.lesson.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;

/**
 * 
 * @author wangsch
 * @date 2017年2月14日
 */
public class LessonModel extends AbstractModel<Integer>{
	public enum LessonKeys {
		status,
		course_id,
		teacher_id,
		student_id,
		date,
		time,
		
		// data字段
		student,
		teacher,
		course,
		cnt,
		label, 
		video_uploaded,
		
		homework_status,
		student_reaction_status,
		teacher_reaction_status,
	}
	
	
	private int courseId;
	private int teacherId;
	private int studentId;
	private int chapterId;
	private String date;
	private String time;
	
	int homeworkStatus;
	int studentReactionStatus;
	int teacherReactionStatus;
	
	public LessonModel(Integer id, String data, long createTime, 
			int status, int homeworkStatus, int studentReactionStatus, int teacherReactionStatus, 
			int courseId, int teacherId, int chapterId, int studentId, String date, String time) {
		super(id, data, createTime, status);
		this.homeworkStatus = homeworkStatus;
		this.studentReactionStatus = studentReactionStatus;
		this.teacherReactionStatus = teacherReactionStatus;
		this.courseId = courseId;
		this.teacherId = teacherId;
		this.chapterId = chapterId;
		this.studentId = studentId;
		this.date = date;
		this.time = time;
	}
	
	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getTeacherId() {
		return teacherId;
	}
	
	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}
	
	public Integer getChapterId() {
		return chapterId;
	}
	
	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	
	public int getHomeworkStatus() {
		return homeworkStatus;
	}

	public void setHomeworkStatus(int homeworkStatus) {
		this.homeworkStatus = homeworkStatus;
	}

	public int getStudentReactionStatus() {
		return studentReactionStatus;
	}

	public void setStudentReactionStatus(int studentReactionStatus) {
		this.studentReactionStatus = studentReactionStatus;
	}

	public int getTeacherReactionStatus() {
		return teacherReactionStatus;
	}

	public void setTeacherReactionStatus(int teacherReactionStatus) {
		this.teacherReactionStatus = teacherReactionStatus;
	}

	public void setChapterId(int chapterId) {
		this.chapterId = chapterId;
	}

	public String getLabel() {
		return ModelUtils.getString(this, LessonKeys.label);
	}
	
	public Integer getCnt() {
		return ModelUtils.getInt(this, LessonKeys.cnt);
	}
	
	public String getTeacher() {
		return ModelUtils.getString(this, LessonKeys.teacher);
	}
	
	public String getStudent() {
		return ModelUtils.getString(this, LessonKeys.student);
	}
	
	public String getCourse() {
		return ModelUtils.getString(this, LessonKeys.course);
	}
	
	public Boolean getVideoUploaded() {
		return ModelUtils.getBoolean(this, LessonKeys.video_uploaded, false);
	}
	
	// 
	private Integer teacherOnlineStatus;
	private Integer studentOnlineStatus;

	public Integer getTeacherOnlineStatus() {
		return teacherOnlineStatus;
	}

	public void setTeacherOnlineStatus(Integer teacherOnlineStatus) {
		this.teacherOnlineStatus = teacherOnlineStatus;
	}

	public Integer getStudentOnlineStatus() {
		return studentOnlineStatus;
	}

	public void setStudentOnlineStatus(Integer studentOnlineStatus) {
		this.studentOnlineStatus = studentOnlineStatus;
	}
}
