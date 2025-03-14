package com.youshi.zebra;

import org.junit.Test;

import com.dorado.framework.utils.DoradoBeanFactory;
import com.youshi.zebra.passport.service.UserPassportService;

/**
 * 
 * @author wangsch
 * @date 2017年4月11日
 */
public class TicketTest {
	@Test
	public void test() {
		UserPassportService passportService = DoradoBeanFactory.getBean(UserPassportService.class);
		String ticket = passportService.createTicketWithRand(3);	// 创建ticket
		System.out.println(ticket);
	}
	
//	@Test
//	public void userid() {
//		UserPassportService bean = DoradoBeanFactory.getBean(UserPassportService.class);
//		Integer userId = bean.verifyTicket("MJssLWZ3xdoet_uf9g4hHxHm-e6IMT4ISBgUJTcoaeE_l9jZP54A8G5rKTXcaVhH");
//		System.out.println(userId);
//	}
}
