package com.youshi.zebra.core.web.taglib;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 日期、时间相关函数
 * 
 * @author wangsch
 * @date 2016-09-12
 */
public class DateTimeFunction {

    private static final long SECOND = TimeUnit.SECONDS.toMillis(1);

    private static final long MINUTE = TimeUnit.MINUTES.toMillis(1);

    private static final long HOUR = TimeUnit.HOURS.toMillis(1);

    private static final long DAYS = TimeUnit.DAYS.toMillis(1);

    private static final long MONTH = TimeUnit.DAYS.toMillis(30);

    private static final long YEAR = TimeUnit.DAYS.toMillis(365);
    
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private static final SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
    
    public static String datetime(long date) {
    	if(date == 0) {
    		return null;
    	}
    	return sdf.format(new Date(date));
    }
    
    public static String date(long date) {
    	if(date == 0) {
    		return null;
    	}
    	return dateSdf.format(new Date(date));
    }
    
    
    /**
     * 格式：和当前时间比，过了多长时间。
     * 
     * @param timeMillis		毫秒
     * @return						字符串格式
     */
    public static String beforeTime(long timeMillis) {
        long last = System.currentTimeMillis() - timeMillis;
        if (last > YEAR) {
            return last / YEAR + "年前";
        } else if (last > MONTH) {
            return last / MONTH + "月前";
        } else if (last > DAYS) {
            return last / DAYS + "天前";
        } else if (last > HOUR) {
            return last / HOUR + "小时前";
        } else if (last > MINUTE) {
            return last / MINUTE + "分钟前";
        } else {
            return last / SECOND + "秒前";
        }
    }

}
