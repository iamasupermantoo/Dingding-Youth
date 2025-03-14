package com.youshi.zebra.lesson.model;

/**
 * 一节课，各个参与者的ready状态
 * 
 * @author wangsch
 * @date 2017年2月17日
 */
public class LessonReadyStatusModel {
	private int id;
	
	private int courseId;
	
	private int lessonId;
	
	private int teacherReady;
	
	private int assistantReady;
	
	private int studentReady;
	
	private String schedule;
	
	public LessonReadyStatusModel(){
	}

	public LessonReadyStatusModel(int id, int courseId, int lessonId, int teacherReady, int assistantReady,
			int studentReady, String schedule) {
		this.id = id;
		this.courseId = courseId;
		this.lessonId = lessonId;
		this.teacherReady = teacherReady;
		this.assistantReady = assistantReady;
		this.studentReady = studentReady;
		this.schedule = schedule;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getTeacherReady() {
		return teacherReady;
	}

	public void setTeacherReady(int teacherReady) {
		this.teacherReady = teacherReady;
	}

	public int getAssistantReady() {
		return assistantReady;
	}

	public void setAssistantReady(int assistantReady) {
		this.assistantReady = assistantReady;
	}

	public int getStudentReady() {
		return studentReady;
	}

	public void setStudentReady(int studentReady) {
		this.studentReady = studentReady;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	

}
