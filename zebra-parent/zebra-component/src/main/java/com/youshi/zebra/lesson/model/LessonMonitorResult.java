package com.youshi.zebra.lesson.model;

import java.util.List;

public class LessonMonitorResult {
	private List<LessonModel> preLessons;

	private List<LessonModel> nextLessons;

	private MonitorInfo info;

	public LessonMonitorResult(List<LessonModel> preLessons, List<LessonModel> nextLessons,
			MonitorInfo info) {
		this.preLessons = preLessons;
		this.nextLessons = nextLessons;
		this.info = info;
	}

	public List<LessonModel> getPreLessons() {
		return preLessons;
	}

	public List<LessonModel> getNextLessons() {
		return nextLessons;
	}

	public MonitorInfo getInfo() {
		return info;
	}
}