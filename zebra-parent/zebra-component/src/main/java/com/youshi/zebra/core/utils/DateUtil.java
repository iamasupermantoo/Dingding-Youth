package com.youshi.zebra.core.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 
/**
 * @author lrq
 */
public final class DateUtil {
    /** 采用Singleton设计模式而具有的唯一实例 */
    private static DateUtil instance = new DateUtil();
 
    /** 格式化器存储器 */
    private Map formats;
 
    private DateUtil() {
        resetFormats();
    }
 
    /**
     * 通过缺省日期格式得到的工具类实例
     * 
     * @return <code>DateUtilities</code>
     */
    public static DateUtil getInstance() {
        return instance;
    }
 
    /** Reset the supported formats to the default set. */
    public void resetFormats() {
        formats = new HashMap();
 
        // alternative formats
        formats.put("yyyy-MM-dd HH:mm:ss", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
 
        // alternative formats
        formats.put("yyyy-MM-dd", new SimpleDateFormat("yyyy-MM-dd"));
 
        // XPDL examples format
        formats.put("MM/dd/yyyy HH:mm:ss a", new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a"));
 
        // alternative formats
        formats.put("yyyy-MM-dd HH:mm:ss a", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a"));
 
        // ISO formats
        formats.put("yyyy-MM-dd'T'HH:mm:ss'Z'", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        formats.put("yyyy-MM-dd'T'HH:mm:ssZ", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
        formats.put("yyyy-MM-dd'T'HH:mm:ssz", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz"));
    }
 
    /**
     * 对日期进行格式化 格式为：yyyy-MM-dd HH:mm:ss
     * 
     * @param date
     *            需格式化的日期
     * @return 格式化后的字符串
     */
    public String format(Date date) {
        Object obj = formats.get("yyyy-MM-dd HH:mm:ss");
        if (obj == null) {
            obj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        return ((DateFormat) obj).format(date);
    }
 
    /**
     * 解析字符串到日期型
     * 
     * @param dateString
     *            日期字符串
     * @return 返回日期型对象
     */
    public Date parse(String dateString) {
        Iterator iter = formats.values().iterator();
        while (iter.hasNext()) {
            try {
                return ((DateFormat) iter.next()).parse(dateString);
            } catch (ParseException e) {
                // do nothing
            }
        }
        return null;
    }
 

 
    /** add by wangwc 2007-05-12 */
 
    public static final long SECOND = 1000;
 
    public static final long MINUTE = SECOND * 60;
 
    public static final long HOUR = MINUTE * 60;
 
    public static final long DAY = HOUR * 24;
 
    public static final long WEEK = DAY * 7;
 
    public static final long YEAR = DAY * 365;
 
    private static Log log = LogFactory.getLog(DateUtil.class);
 
    private static DateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
 
    /**
     * 解析日期
     * 
     * @param date
     *            日期字符串
     * @param pattern
     *            日期格式
     * @return
     */
    public static Date parse(String date, String pattern) {
        Date resultDate = null;
        try {
            resultDate = new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            log.error(e);
        }
        return resultDate;
    }
 
    /**
     * 解析日期 yyyy-MM-dd
     * 
     * @param date
     *            日期字符串
     * @param pattern
     *            日期格式
     * @return
     */
    public static Date parseSimple(String date) {
        Date resultDate = null;
        try {
            resultDate = YYYY_MM_DD.parse(date);
        } catch (ParseException e) {
            log.error(e);
        }
        return resultDate;
    }
 
    /**
     * 格式化日期字符串
     * 
     * @param date
     *            日期
     * @param pattern
     *            日期格式
     * @return
     */
    public static String format(Date date, String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
 

 
    /**
     * 取得当前日期
     * 
     * @return
     */
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
 
    /**
     * @param offsetYear
     * @return 当前时间 + offsetYear
     */
    public static Timestamp getTimestampExpiredYear(int offsetYear) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR, offsetYear);
        return new Timestamp(now.getTime().getTime());
    }
 
    /**
     * @param offsetMonth
     * @return 当前时间 + offsetMonth
     */
    public static Timestamp getCurrentTimestampExpiredMonth(int offsetMonth) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MONTH, offsetMonth);
        return new Timestamp(now.getTime().getTime());
    }
 
    /**
     * @param offsetDay
     * @return 当前时间 + offsetDay
     */
    public static Timestamp getCurrentTimestampExpiredDay(int offsetDay) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, offsetDay);
        return new Timestamp(now.getTime().getTime());
    }
 
    /**
     * @param offsetSecond
     * @return 当前时间 + offsetSecond
     */
    public static Timestamp getCurrentTimestampExpiredSecond(int offsetSecond) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.SECOND, offsetSecond);
        return new Timestamp(now.getTime().getTime());
    }
 
    /**
     * @param offsetDay
     * @return 指定时间 + offsetDay
     */
    public static Timestamp getTimestampExpiredDay(Date givenDate, int offsetDay) {
        Calendar date = Calendar.getInstance();
        date.setTime(givenDate);
        date.add(Calendar.DATE, offsetDay);
        return new Timestamp(date.getTime().getTime());
    }
 
    /**
     * 实现ORACLE中ADD_MONTHS函数功能
     * 
     * @param offsetMonth
     * @return 指定时间 + offsetMonth
     */
    public static Timestamp getTimestampExpiredMonth(Date givenDate, int offsetMonth) {
        Calendar date = Calendar.getInstance();
        date.setTime(givenDate);
        date.add(Calendar.MONTH, offsetMonth);
        return new Timestamp(date.getTime().getTime());
    }
 
    /**
     * @param offsetSecond
     * @return 指定时间 + offsetSecond
     */
    public static Timestamp getTimestampExpiredSecond(Date givenDate, int offsetSecond) {
        Calendar date = Calendar.getInstance();
        date.setTime(givenDate);
        date.add(Calendar.SECOND, offsetSecond);
        return new Timestamp(date.getTime().getTime());
    }
     
    /**
     * @param offsetSecond
     * @return 指定时间 + offsetSecond
     */
    public static Timestamp getTimestampExpiredHour(Date givenDate, int offsetHour) {
        Calendar date = Calendar.getInstance();
        date.setTime(givenDate);
        date.add(Calendar.HOUR, offsetHour);
        return new Timestamp(date.getTime().getTime());
    }
 
    /**
     * @return 当前日期 yyyy-MM-dd
     */
    public static String getCurrentDay() {
        return DateUtil.format(new Date(), "yyyy-MM-dd");
    }
 
    /**
     * @return 当前的时间戳 yyyy-MM-dd HH:mm:ss
     */
    public static String getNowTime() {
        return DateUtil.getInstance().format(new Date());
    }
 
    /**
     * @return 给出指定日期的月份的第一天
     */
    public static Date getMonthFirstDay(Date givenDate) {
        Date date = DateUtil.parse(DateUtil.format(givenDate, "yyyy-MM"), "yyyy-MM");
        return date;
    }
 
    /**
     * @return 给出指定日期的月份的最后一天
     */
    public static Date getMonthLastDay(Date givenDate) {
        Date firstDay = getMonthFirstDay(givenDate);
        Date lastMonthFirstDay = DateUtil.getTimestampExpiredMonth(firstDay, 1);
        Date lastDay = DateUtil.getTimestampExpiredDay(lastMonthFirstDay, -1);
        return lastDay;
    }
    
    /**
     * @return 给出指定日期的星期的星期一
     */
    public static Date getWeekFirstDay() {
    	SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");  
    	Calendar c = Calendar.getInstance();  
    	c.setFirstDayOfWeek(Calendar.MONDAY);  
    	c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);  
    	String monday = d.format(c.getTime());
    	Date parseSimple = parseSimple(monday);
        return parseSimple;
    }
    
    public static Date getThisDay() {
    	SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");   
    	String thisday = d.format(new Date());
    	Date parseSimple = parseSimple(thisday);
        return parseSimple;
    }
    
    
    
    
    
    
    
    
    
    // 获取当前时间所在年的周数
    public static int getWeekOfYear(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);

        return c.get(Calendar.WEEK_OF_YEAR);
    }

    // 获取当前时间所在年的最大周数
    public static int getMaxWeekNumOfYear(int year) {
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);

        return getWeekOfYear(c.getTime());
    }

    // 获取某年的第几周的开始日期
    public static Date getFirstDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return getFirstDayOfWeek(cal.getTime());
    }

    // 获取某年的第几周的结束日期
    public static Date getLastDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return getLastDayOfWeek(cal.getTime());
    }

    // 获取当前时间所在周的开始日期
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    // 获取当前时间所在周的结束日期
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
        return c.getTime();
    }
    
    // 获取当前时间的下一天
    public static Date getNextDay(Date date) {
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        c.add(c.DATE,1);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
        return c.getTime();
    }
    
    // 获取当前时间的上一天
    public static Date getLastDay(Date date) {
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        c.add(c.DATE,1);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
        return c.getTime();
    }
    
    
    @SuppressWarnings("deprecation")
	public static int getAge(Date birth) {
        return new Date().getYear() - birth.getYear();
    }
    
    /**
     * 两个日期相隔天数
     * @param start
     * @param end
     * @return
     */
    public static int getIntervalDays( Date start , Date end ) {
    	int days = (int) ((end.getTime() - start.getTime()) / DAY);
    	return days;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*public static void main(String[] args) {
    	Date monthFirstDay = getMonthFirstDay(new Date());
    	System.out.println(monthFirstDay);
    	
    	SimpleDateFormat simdf = new SimpleDateFormat("yyyy-MM-dd");
    	 
    	Calendar cal = Calendar.getInstance();
    	System.out.println("现在时间："+simdf.format(cal.getTime()));
    	//分别获取年、月、日
    	System.out.println("年："+cal.get(cal.YEAR));
    	System.out.println("月："+(cal.get(cal.MONTH)+1));//老外把一月份整成了0，翻译成中国月份要加1
    	System.out.println("日："+cal.get(cal.DATE));
    	 
    	cal.set(cal.DAY_OF_WEEK, cal.MONDAY);
    	String weekhand = simdf.format(cal.getTime());
    	System.out.println("当前时间所在周周一日期："+weekhand);
    	//cal.set(cal.DAY_OF_WEEK, cal.SUNDAY);这个不符合中国人的时间观，老外把上周周日定为一周的开始。
    	 
    	cal.set(Calendar.DATE, cal.get(cal.DATE) + 6);
    	String weeklast = simdf.format(cal.getTime());
    	System.out.println("当前时间所在周周日日期："+weeklast);
    	
    	
    	SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");  
    	Calendar c = Calendar.getInstance();  
    	c.setFirstDayOfWeek(Calendar.MONDAY);  
    	c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);  
    	String monday = d.format(c.getTime());    
    	
    	System.out.println(  
    			new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(
    					Calendar.getInstance().get(Calendar.getInstance().DATE)
    								));
    	
    	System.out.println( getFirstDayOfWeek(2007, 32) );
    	
	}*/
 
}
