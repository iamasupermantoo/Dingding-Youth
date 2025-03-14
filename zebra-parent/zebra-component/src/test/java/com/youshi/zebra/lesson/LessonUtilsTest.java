package com.youshi.zebra.lesson;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.youshi.zebra.lesson.utils.LessonTimeUtils;

/**
 * 
 * @author wangsch
 * @date 2017年2月17日
 */
public class LessonUtilsTest {
	@Test
	public void test() {
		String periodTime = LessonTimeUtils.periodTime("13:30", "14:20");
		Assert.assertEquals("13:30 - 14:20", periodTime);
	}
	
	@Test
	public void test2() {
		boolean timeConflict = LessonTimeUtils.isTimeConflict("13:30", "14:20", Arrays.asList(
				LessonTimeUtils.periodTime("16:30", "17:20"),
				LessonTimeUtils.periodTime("19:00", "19:50"),
				LessonTimeUtils.periodTime("13:20", "14:10")
				));
		Assert.assertTrue(timeConflict);
		
		timeConflict = LessonTimeUtils.isTimeConflict("13:30", "14:20", Arrays.asList(
				LessonTimeUtils.periodTime("16:30", "17:20"),
				LessonTimeUtils.periodTime("19:00", "19:50")
				));
		Assert.assertFalse(timeConflict);
	}
}
