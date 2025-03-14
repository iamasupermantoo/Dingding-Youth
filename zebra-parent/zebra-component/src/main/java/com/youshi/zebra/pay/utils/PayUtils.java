package com.youshi.zebra.pay.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.youshi.zebra.core.constants.config.ListConfigKey;

/**
 * 
 * @author wangsch
 * @date 2017年8月10日
 */
public class PayUtils {
	public static boolean inWhite(int userId) {
		List<Object> userIds = ListConfigKey.PAY_TEST_USER_IDS.get();
		for (Object object : userIds) {
			Integer blackUserId = (Integer)object;
			if(blackUserId.intValue() == userId) {
				return true;
			}
		}
		return false;
	}
	
	public static List<Integer> payTestUserIds() {
		List<Integer> result = new ArrayList<>();
		List<Object> userIds = ListConfigKey.PAY_TEST_USER_IDS.get();
		if(CollectionUtils.isEmpty(userIds)) {
			return Collections.emptyList();
		}
		
		for (Object object : userIds) {
			Integer userId = (Integer)object;
			result.add(userId);
		}
		
		return result;
	}
}
