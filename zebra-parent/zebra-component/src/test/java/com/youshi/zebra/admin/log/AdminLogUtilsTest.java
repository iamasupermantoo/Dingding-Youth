package com.youshi.zebra.admin.log;

import org.junit.Test;

import com.dorado.framework.utils.DoradoBeanFactory;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.google.common.collect.ImmutableMap;
import com.youshi.zebra.admin.adminuser.service.impl.AdminUserPassportServiceImpl;
import com.youshi.zebra.admin.log.constants.AdminLogType;
import com.youshi.zebra.admin.log.utils.AdminLogUtils;

/**
 * 
 * @author wangsch
 * @date 2017年2月27日
 */
public class AdminLogUtilsTest {
	@Test
	public void test() {
		AdminLogUtils bean = DoradoBeanFactory.getBean(AdminLogUtils.class);
		bean.record(1, AdminLogType.SpammerDictAdd, 
				WebRequestContext.ipv4ToLong("192.168.1.1"),
				ImmutableMap.<String, Object>builder()
				.put("spam", "中国国民党当通局")
				.put("desc", "国民党中统，军统")
				.build());
	}
	
	@Test
	public void teset2() {
		System.out.println(AdminUserPassportServiceImpl.encodeMd5Password("zebra_Admin2017"));
	}
	
}
