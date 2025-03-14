package com.youshi.zebra.lesson.model;

/**
 * 用户的时间表
 * 
 * @author wangsch
 * @date 2017年2月17日
 */
public class UserScheduleModel {
	private int id;
	
	private int userId;
	
	private String date;
	
	private String schedule;
	
	public UserScheduleModel(){
	}
	
	public UserScheduleModel(int id, int userId, String date, String schedule) {
		this.id = id;
		this.userId = userId;
		this.date = date;
		this.schedule = schedule;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
}
