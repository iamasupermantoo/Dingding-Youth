package com.youshi.zebra.stats.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dorado.framework.constants.InProduction;
import com.google.common.base.Splitter;
import com.youshi.zebra.core.constants.config.RawStringConfigKey;
import com.youshi.zebra.core.utils.DateTimeUtils;

/**
 * 
 * @author wangsch
 * @date 2017年7月20日
 */
public class StatsUtils {
	/**
	 * 获取今天的时间戳，毫秒。为了测试方便，允许修改当前时间
	 * 
	 * @return	当前时间毫秒值
	 */
	public static long todayTimeMills() {
		long currTime = System.currentTimeMillis();
		if(!InProduction.get()) {
			String mockToday = RawStringConfigKey.STATS_TODAY.get();
			if(StringUtils.isNotEmpty(mockToday)) {
				currTime = DateTimeUtils.parseDate(mockToday);
			}
		}
		return currTime;
	}
	
	/**
	 * 获取今天的日期。为了测试方便，允许修改当前时间
	 * 
	 * @return	当前日期
	 */
	public static String today() {
		if(!InProduction.get()) {
			String mockToday = RawStringConfigKey.STATS_TODAY.get();
			if(StringUtils.isNotEmpty(mockToday)) {
				return mockToday;
			}
		}
		return DateTimeUtils.getDate(System.currentTimeMillis());
	}
	
	
	
	public static final List<Integer> SHOW_RET_RATE_DAYS = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 14, 21, 28);
 	
	/**
	 * 解析留存率，n天后保留率，记做：afterDay，保留率百分比，保留1位小数，记为retRate。每个数据表示为：
	 * “afterDay:retRate”的形式，如：“1:10.5”表示，一天后保留率为10.5%，多天的用逗号分隔组成一个大的字符串。
	 * 如：1:10.5, 2:20.5, 3:90.5
	 * 
	 * 需要将retRate提取出来，组成集合返回
	 * 
	 * @param raw	原始字符串
	 * @return		存留率集合
	 */
	public static List<String> parseRetRates(String raw) {
		Iterable<String> it = Splitter.onPattern("\\s*,\\s*").split(raw);
		Map<Integer, String> rates = new HashMap<>();
		for (String oneRate : it) {
			if(StringUtils.isEmpty(oneRate)) {
				continue;
			}
			String[] parts = oneRate.split(":");
			int afterDay = Integer.parseInt(parts[0]);
			String retRate = parts[1];
			rates.put(afterDay, retRate);
		}
		
		List<String> result = new ArrayList<>();
		for(int afterDay : SHOW_RET_RATE_DAYS) {
			String retRate = rates.get(afterDay);
			result.add(StringUtils.isNotEmpty(retRate) ? retRate : "");
		}
		return result;
	}
}
