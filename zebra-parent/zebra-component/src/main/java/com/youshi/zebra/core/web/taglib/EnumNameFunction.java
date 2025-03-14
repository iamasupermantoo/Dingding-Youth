package com.youshi.zebra.core.web.taglib;

import com.dorado.gotopage.constant.GotoPage;
import com.youshi.zebra.admin.log.constants.AdminLogType;
import com.youshi.zebra.course.constants.CourseStatus;
import com.youshi.zebra.lesson.constants.LessonStatus;
import com.youshi.zebra.media.constants.MediaType;
import com.youshi.zebra.order.constants.ChatResult;
import com.youshi.zebra.order.constants.OrderStatus;
import com.youshi.zebra.order.constants.PayStatus;
import com.youshi.zebra.pay.constants.PayChannel;
import com.youshi.zebra.recommend.constants.BannerStatus;
import com.youshi.zebra.recommend.constants.RecommendFeedType;
import com.youshi.zebra.user.constant.Gender;
import com.youshi.zebra.user.constant.UserStatus;
import com.youshi.zebra.user.constant.UserType;

/**
 * 
 * 把int类型的枚举值，换成String的可显示字符串。{@code dorado-enum.tld}
 * 
 * @author wangsch
 * @date 2016年11月6日
 * 
 *
 */
public class EnumNameFunction {
	public static String userType(Integer value) {
		return UserType.fromValue(value).getName();
	}
	
	public static String userStatus(Integer value) {
		return UserStatus.fromValue(value).getName();
	}
	
	public static String recommendFeedType(Integer value) {
		return RecommendFeedType.fromValue(value).getName();
	}
	
	public static String bannerStatus(Integer value) {
		return BannerStatus.fromValue(value).getName();
	}
	public static String gotoPageType(Integer value) {
		return GotoPage.fromValue(value).getName();
	}
	
	public static String logType(Integer value) {
		return AdminLogType.fromValue(value).getName();
	}
	
	public static String gender(Integer value) {
		if(value == null) {
			return "未知";
		}
		Gender e = Gender.fromValue(value);
		return e != null ? e.getName() : "未知";
	}
	
	public static String mediaType(Integer value) {
		MediaType type = MediaType.fromValue(value);
		return type.getName();
	}
	
	public static String courseStatus(Integer value) {
		if(value == null) {
			return "未知";
		}
		CourseStatus e = CourseStatus.fromValue(value);
		return e != null ? e.getName() : "未知";
	}
	
	public static String lessonStatus(Integer value) {
		if(value == null) {
			return "未知";
		}
		LessonStatus e = LessonStatus.fromValue(value);
		return e != null ? e.getName() : "未知";
	}
	
	public static String orderStatus(Integer value) {
		if(value == null) {
			return "未知";
		}
		OrderStatus e = OrderStatus.fromValue(value);
		return e != null ? e.getName() : "未知";
	}
	
	public static String payStatus(Integer value) {
		if(value == null) {
			return "未知";
		}
		PayStatus e = PayStatus.fromValue(value);
		return e != null ? e.getName() : "未知";
	}
	
	public static String chatResult(Integer value) {
		if(value == null) {
			return "未知";
		}
		ChatResult e = ChatResult.fromValue(value);
		return e != null ? e.getName() : "未知";
	}
	
	public static String payChannel(Integer value) {
		if(value == null) {
			return "未知";
		}
		PayChannel e = PayChannel.fromValue(value);
		return e != null ? e.getName() : "未知";
	}
}
