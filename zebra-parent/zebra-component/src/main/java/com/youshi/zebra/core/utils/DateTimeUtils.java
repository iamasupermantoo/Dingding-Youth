package com.youshi.zebra.core.utils;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.youshi.zebra.exception.base.DoradoRuntimeException;

/**
 * 
 * 日期，时间工具
 * 
 * @author wangsch
 * @date 2016-09-28
 */
public class DateTimeUtils {
	
	public static final String getDateTime(long longTime) {
		if(longTime == 0) {
			return null;
		}
		return DateFormatUtils.format(longTime, "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 
	 * 获取日期／时间，long类型转换为字符串类型
	 * 
	 * @param longTime
	 * @param minUnit
	 * @return
	 */
	public static final String getDateTime(long longTime, TimeUnit minUnit) {
		String result = null;
		switch (minUnit) {
		case DAYS:
			result = DateFormatUtils.format(longTime, "yyyy-MM-dd");
			break;
		case MINUTES:
			result = DateFormatUtils.format(longTime, "yyyy-MM-dd HH:mm");
			break;
		case SECONDS:
			result = DateFormatUtils.format(longTime, "yyyy-MM-dd HH:mm:ss");
			break;
		default:
			throw new IllegalArgumentException("Not supported time unit: " + minUnit);
		}
		
		return result;
	}
	
	/**
	 * 字符串，转换成时间戳，毫秒
	 * 
	 * @param dateTime				字符串格式日期、时间
	 * @param format						格式
	 * @return									毫秒值
	 * @throws ParseException		不能按指定的{@code format}解析{@code dateTime}
	 */
	public static final long timestamp(String dateTime, String format) throws ParseException {
		return DateUtils.parseDate(dateTime, format).getTime();
	}
	
	public static long parseDate(String strTime) {
		try {
			return DateUtils.parseDate(strTime, "yyyy-MM-dd").getTime();
		} catch (ParseException e) {
			throw new DoradoRuntimeException();
		}
	}
	
	/**
	 * 获取日期
	 * 
	 * @param longTime	long类型时间，毫秒
	 * @return						形如“2016-09-28”格式
	 */
	public static final String getDate(long longTime) {
		if(longTime == 0) {
			return null;
		}
		return DateFormatUtils.format(longTime, "yyyy-MM-dd");
	}
	
	public static long parseDateTime(String strTime) {
		try {
			return DateUtils.parseDate(strTime, "yyyy-MM-dd HH:mm:ss").getTime();
		} catch (ParseException e) {
			throw new DoradoRuntimeException();
		}
	}
	
	public static final String getDateTime(long longTime, String format) {
		return DateFormatUtils.format(longTime, format);
	}
	
	/**
	 * @see #agoTime(long, TimeUnit)
	 */
	public static final String agoTime(long time) {
		return agoTime(time, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 和当前时间比，返回经过了多长时间。
	 * 
	 * @param time			时间，long型
	 * @param	timeUnit	{@code time}参数的时间单位
	 * @return				经过的时间，字符串
	 */
	public static final String agoTime(long time, TimeUnit timeUnit) {
		long escapeTime = System.currentTimeMillis() - timeUnit.toMillis(time);
		if(escapeTime < 0) {
			throw new IllegalArgumentException("Publish time SHOULD NOT greater than current Time.");
		}
		
		long second = escapeTime / TimeUnit.SECONDS.toMillis(1);
		if(second < 60) {
			return "刚刚";
		}
		
		long minute = escapeTime / TimeUnit.MINUTES.toMillis(1);
		if(minute < 60) {
			return minute + "分钟前";
		}

		long hour = escapeTime / TimeUnit.HOURS.toMillis(1);
		if(hour < 24) {
			return hour + "小时前";
		}
		
		long day = escapeTime / TimeUnit.DAYS.toMillis(1);
		if(day < 31) {
			return day + "天前";
		}

		
		long month = escapeTime / TimeUnit.DAYS.toMillis(30);
		if(month < 12) {
			return month + "个月前";
		}
		
		long year = escapeTime / TimeUnit.DAYS.toMillis(365);
		return year + "年前";
	}
}
