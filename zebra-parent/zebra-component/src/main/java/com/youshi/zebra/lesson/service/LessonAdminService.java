package com.youshi.zebra.lesson.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.youshi.zebra.core.utils.DateTimeUtils;
import com.youshi.zebra.lesson.constants.LessonStatus;
import com.youshi.zebra.lesson.dao.LessonDAO;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.lesson.model.LessonModel.LessonKeys;
import com.youshi.zebra.lesson.model.LessonMonitorResult;
import com.youshi.zebra.lesson.model.MonitorInfo;
import com.youshi.zebra.lesson.utils.LessonTimeUtils;
import com.youshi.zebra.live.model.RoomInfoModel;
import com.youshi.zebra.live.service.LiveService;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年2月14日
 */
@Service
public class LessonAdminService {
	private static final Logger logger = LoggerFactory.getLogger(LessonAdminService.class);
	
	@Autowired
	private LessonService lessonService;
	
	@Autowired
	private LessonDAO lessonDAO;
	
	@Autowired
	private LiveService liveService;
	
	public PageView<LessonModel, HasUuid<Integer>> getLessons(Integer courseId, LessonStatus status,
			Integer cursor, Integer limit) {
		WhereClause params = WhereClause.create();
		if(courseId != null) {
			params.and().eq(LessonKeys.course_id, courseId);
		}
		if(status != null) {
			params.and().eq(LessonKeys.status, status.getValue());
		}
		
		PageView<LessonModel, HasUuid<Integer>> page = lessonService
				.getByCursor(cursor, limit, params);
		
		return page;
	}
	
	public LessonMonitorResult getLessonMonitor(Integer cursor, Integer limit) {
		long currTime = System.currentTimeMillis();
		List<LessonModel> preLessons = lessons(PRE, currTime, cursor, limit);
		List<LessonModel> nextLessons = lessons(NEXT, currTime, cursor, limit);
		
		// TODO 异常数
		int excepCount = 0;
		MonitorInfo info = new MonitorInfo(preLessons.size(), nextLessons.size(), excepCount, currTime);
		return new LessonMonitorResult(preLessons, nextLessons, info);
	}
	
	public List<LessonModel> lessons(String flag, long currTime, Integer cursor, Integer limit) {
		Collection<String> times = computeTimes(flag, currTime, PERIOD_COUNT);
		logger.info("Will monitor times period: {}, flag: {}", times, flag);
		
		WhereClause params = WhereClause.create()
				.and().eq(LessonKeys.date, DateTimeUtils.getDate(currTime))
				.and().in(LessonKeys.time, times);
		PageView<LessonModel, HasUuid<Integer>> page = lessonService
				.getByCursor(cursor, limit, params);
		
		List<LessonModel> list = page.getList();
		// TODO 根据time sort一下
		
		// 注入老师、学生的直播状态
		for (LessonModel lesson : list) {
			RoomInfoModel roomInfo = liveService.getRoomInfo(lesson.getCourseId(), lesson.getId());
			lesson.setStudentOnlineStatus(roomInfo.getStudentOnlineStatus());
			lesson.setTeacherOnlineStatus(roomInfo.getTeacherOnlineStatus());
		}
		
		return list;
	}
	
	public static final String PRE = "pre";
	public static final String NEXT = "next";

	public static final int PERIOD_COUNT = 3;
	
	/**
	 * 根据当前时间，计算时间段
	 * 
	 * @param flag			pre或者next，代表向前或者向后
	 * @param currTime		当前时间毫秒值
	 * @param periodCount	向前或向后，取多少个时间段
	 * @return				时间段集合，如：[10:00 - 10:30, 10:30 - 11:00, 11:00 - 11:30]
	 */
	private Collection<String> computeTimes(String flag, long currTime, int periodCount) {
		Collection<String> result = new ArrayList<>();
		int minute = Integer.parseInt(DateFormatUtils.format(currTime, "mm"));
		
		Date curr = new Date(currTime);
		Date base = null;
		if(minute > 0 && minute <30) {
			// base = setMinutes to 30
			base = DateUtils.setMinutes(curr, 30);
		} else if(minute > 30 && minute < 59) {
			// base = ceiling
			base = DateUtils.ceiling(curr, Calendar.HOUR);
		} else if(minute == 0 || minute == 30) {
			// base = curr
			base =  new Date(currTime);
		}
		
		if(PRE.equalsIgnoreCase(flag)) {
			Date startTime = new Date(base.getTime());
			Date endTime = new Date(base.getTime());
			for(int i=0; i<periodCount; i++) {
				endTime = startTime;
				String end = DateFormatUtils.format(endTime, "HH:mm");
				
				startTime = DateUtils.addMinutes(startTime, -30);
				String start = DateFormatUtils.format(startTime, "HH:mm");
				
				String time = LessonTimeUtils.concat(start, end);
				result.add(time);
			}
		} else if(NEXT.equalsIgnoreCase(flag)) {
			Date startTime = new Date(base.getTime());
			Date endTime = new Date(base.getTime());
			for(int i=0; i<periodCount; i++) {
				startTime = endTime;
				String start = DateFormatUtils.format(startTime, "HH:mm");
				
				endTime = DateUtils.addMinutes(startTime, 30);
				String end = DateFormatUtils.format(endTime, "HH:mm");
				
				String time = LessonTimeUtils.concat(start, end);
				result.add(time);
			}
		} else {
			throw new IllegalArgumentException();
		}
		
		return result;
	}
	
	// ----------------------------------------- private methods -----------------------------------------
	

}
