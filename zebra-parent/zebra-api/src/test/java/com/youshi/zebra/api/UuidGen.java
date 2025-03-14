package com.youshi.zebra.api;

import java.util.Arrays;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.youshi.zebra.audio.model.AudioModel;
import com.youshi.zebra.core.ZebraSystemInitBean;
import com.youshi.zebra.core.utils.DateTimeUtils;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.homework.model.HomeworkModel;
import com.youshi.zebra.image.model.ImageModel;
import com.youshi.zebra.user.model.UserModel;

/**
 * 
 * @author wangsch
 * @date 2017年2月9日
 */
public class UuidGen {

	@Test
	public void time() {
		String dateTime = DateTimeUtils.getDateTime(1488536782062L);
		System.out.println(dateTime);
	}

	@Test
	public void test() {
		new ZebraSystemInitBean().init();
		String uuid = UuidUtils.getUuid(UserModel.class, 110089);
		System.out.println(uuid);
//		System.out.println(UuidUtils.getId(AudioModel.class, "FCNC-zHElhA9RzEFBVF2BQ"));
	}

	@Test
	public void genDeskey() {
		byte[] bytes = RandomUtils.nextBytes(8);

		System.out.println(Arrays.toString(bytes));

		// System.out.println(Arrays.asList(bytes));
	}

	@Test
	public void test2() {
		System.out.println(DateTimeUtils.getDateTime(1488349201656L));
	}
}
