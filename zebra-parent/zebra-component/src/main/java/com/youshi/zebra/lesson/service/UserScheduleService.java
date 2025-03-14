package com.youshi.zebra.lesson.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.utils.DoradoMapperUtils;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.exception.base.DoradoRuntimeException;
import com.youshi.zebra.lesson.dao.UserScheduleDAO;
import com.youshi.zebra.lesson.model.UserScheduleModel;
import com.youshi.zebra.lesson.utils.LessonTimeUtils;

/**
 * 
 * 
 * 
 * @author wangsch
 * @date 2017年2月17日
 */
@Service
public class UserScheduleService {
	
	@Autowired
	private UserScheduleDAO userScheduleDAO;
	
	public void addTime(Integer userId, String date, String time) {
		operateTime(userId, date, time, TimeOperate.ADD);
	}
	
	public void removeTime(Integer userId, String date, String time) {
		operateTime(userId, date, time, TimeOperate.REMOVE);
	}
	
	public boolean containsTime(Integer userId, String date, String start, String end) {
		List<String> times = getTimes(userId, date);
		
		boolean contains = times.contains(LessonTimeUtils.periodTime(start, end));
		return contains;
	}
	
	public List<String> getTimes(Integer userId, String date) {
		UserScheduleModel userSchedule = userScheduleDAO.getOne(userId, date);
		
		if(userSchedule == null) {
			return Collections.emptyList();
		}
		
		List<String> times = DoradoMapperUtils.fromJSON(userSchedule.getSchedule(), ArrayList.class, String.class);
		
		return times;
	}
	
	public enum TimeOperate {
		ADD,
		REMOVE
	}
	
	/**
	 * 
	 * 
	 * 
	 * 
	 * Notes:
	 * 上层调用者，必须保证{@code time}和现有的{@link UserScheduleModel#getSchedule()}时间上没有重叠。
	 * 
	 * @param userId
	 * @param date
	 * @param time
	 * @param oper
	 */
	private void operateTime(Integer userId, String date, String time, TimeOperate oper) {
		UserScheduleModel userSchedule = userScheduleDAO.getOne(userId, date);
		
		List<String> times = null;
		if(userSchedule != null) {
			times = DoradoMapperUtils.fromJSON(userSchedule.getSchedule(), ArrayList.class, String.class);
			boolean succ = false;
			if(oper == TimeOperate.ADD) {
				succ = times.add(time);
			} else if(oper == TimeOperate.REMOVE) {
				succ = times.remove(time);
			}
			if(!succ) {
				throw new DoradoRuntimeException();
			}
		} else {
			times = Collections.singletonList(time);
		}
		
		int c = userScheduleDAO.insertOrUpdate(userId, date, DoradoMapperUtils.toJSON(times));
		DAOUtils.checkAffectRows(c, DAOUtils.INSERT_DUPLICATE_ROWS);
	}
}
