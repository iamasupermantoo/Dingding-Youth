package com.youshi.zebra.lesson.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.dorado.framework.tuple.ThreeTuple;
import com.dorado.framework.tuple.TwoTuple;
import com.youshi.zebra.exception.common.ParamVerifyException;
import com.youshi.zebra.lesson.exception.LessonTimePeriodException;

/**
 * 
 * @author wangsch
 * @date 2017年2月17日
 */
public class LessonTimeUtils {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	private static final int MIDDLE_OFFSET_MINUTES = 25;
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @return		TODO b和c可以颠倒。ThreeTuple a: 学生上课时间，b：老师上课时间，c：助教上课时间
	 */
	public static ThreeTuple<String, String, String> splitTime(String start, String end) {
		try {
			Date startDate = sdf.parse(start);
			Date endDate = sdf.parse(end);
			
			if(endDate.getTime() - startDate.getTime() != TimeUnit.MINUTES.toMillis(50)) {
				throw new LessonTimePeriodException(start + "," + end);
			}
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			cal.add(Calendar.MINUTE, MIDDLE_OFFSET_MINUTES);
			
			Date middleDate = cal.getTime();
			String middle = sdf.format(middleDate);
			
			return new ThreeTuple<String, String, String>(concat(start, end), 
					concat(start, middle), 
					concat(middle, end));
		} catch(ParseException e) {
			throw new LessonTimePeriodException(start + "," + end);
		}
	}
	
	
	
	/**
	 * 把开始时间和结束时间组合起来，形成一个时间段，字符串形式，如：“13:30 - 14:30”。
	 * 
	 * @param start	开始时间
	 * @param end	结束时间
	 * @return		时间段，字符串
	 * 
	 * @throws LessonTimePeriodException 时间段不符合要求
	 */
	public static String periodTime(String start, String end) {
		boolean succ = true; 
		try {
			long startTime = sdf.parse(start).getTime();
			long endTime = sdf.parse(end).getTime();
			
			if(endTime - startTime != TimeUnit.MINUTES.toMillis(50)) {
				succ = false;
			}
		} catch(ParseException e) {
			succ = false;
		}
		if(!succ) {
			throw new LessonTimePeriodException(start + "," + end);
		}
		return concat(start, end);
	}



	/**
	 * @param start
	 * @param end
	 * @return
	 */
	public static String concat(String start, String end) {
		return start + " - " + end;
	}
	
	public static String[] split(String time) {
		return time.split(" - ");
	}
	
	/**
	 * 这个判断start和end组成的时间段（如：13:30-14:20）和times里边的多个时间段（如：15:20-16:10, 18:00-18:50, ...），有没有重叠的时间。<br />
	 * 
	 * 这个算法能正确运行的前提是：
	 * 1: start和end组成的时间段是合法的
	 * 2: times组成的时间段都是合法的，且各个时间段之间没有重叠
	 * 
	 * @param start
	 * @param end
	 * @param times
	 * @return			有重叠返回true，否则返回false
	 */
	public static boolean isTimeConflict(String start, String end, List<String> times) {
//		periodTime(start, end);
		
		for (String time : times) {
			// 直接通过比较字符串大小，判断出start或end是否落在time这个时间段内。
//			if((start.compareTo(time) >= 0 && end.compareTo(time) <= 0) || (end.compareTo(time) > 0)) {
//				return true;
//			}
			
			String[] parts = LessonTimeUtils.split(time);
			String s = parts[0];
			String e = parts[1];
			
//			if((start.compareTo(s) >= 0 && start.compareTo(e) <= 0) 
//					|| (end.compareTo(s) > 0 && end.compareTo(e) <= 0)) {
//				return true;
//			}
			if((start.compareTo(s) > 0 && start.compareTo(e) < 0) 
					|| (end.compareTo(s) > 0 && end.compareTo(e) < 0)) {
				return true;
			}
			
			
		}
		
		return false;
	}
	
	/**
	 * 
	 * 
	 * 
	 * @param month 月份，格式是：yyyy-MM
	 * @return
	 */
	public static TwoTuple<String, String> parsePointsMonth(String month) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date startDate = null;
		try {
			startDate = sdf.parse(month + "-01");
		} catch (ParseException e) {
			throw new ParamVerifyException("Month invalid: " + month);
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		Date endDate = cal.getTime();
		
		return new TwoTuple<>(sdf.format(startDate), sdf.format(endDate));
	}
}
