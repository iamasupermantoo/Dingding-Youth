package com.youshi.zebra.order.utils;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 
 * @author wangsch
 * @date 2017年6月12日
 */
public class OrderUtils {
	public static final String genOrderSn(long currentTime) {
		String random = String.format("%04d", RandomUtils.nextInt(0, 10000)); 
		String date = DateFormatUtils.format(currentTime, "yyyyMMddHHmmss");
		
		// 上层调用者需要对订单id冲突进行容错
		return new StringBuilder("00")
				.append(date)
				.append(random)
				.toString();
	}
}
