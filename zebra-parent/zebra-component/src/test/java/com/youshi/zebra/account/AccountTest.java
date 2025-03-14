package com.youshi.zebra.account;

import org.junit.Test;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.dorado.framework.utils.DoradoBeanFactory;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.youshi.zebra.account.service.AccountService;
import com.youshi.zebra.core.ZebraSystemInitBean;
import com.youshi.zebra.order.constants.ProductType;
import com.youshi.zebra.order.model.OrderModel;
import com.youshi.zebra.order.service.ProductAdminService;
import com.youshi.zebra.order.service.ProductService;

/**
 * 
 * @author wangsch
 * @date 2017年11月9日
 */
public class AccountTest {
	
	
	@Test
	public void createChargeItem() {
		ProductAdminService bean = DoradoBeanFactory.getBean(ProductAdminService.class);
		bean.createChargeItem("apple_product1", 1000, 1000, 10);
	}
	
	@Test
	public void createProductCost() {
		ProductAdminService bean = DoradoBeanFactory.getBean(ProductAdminService.class);
		bean.createProductCost(3, 3, "liveuuid", ProductType.OPEN_COURSE.getValue());
	}
	
	@Test
	public void uuid() {
		new ZebraSystemInitBean().init();
		System.out.println(UuidUtils.getUuid(OrderModel.class, 88));
	}
	
	@Test
	public void incrAccount() {
		AccountService bean = DoradoBeanFactory.getBean(AccountService.class);
		bean.charge(110088, 110);
	}
	
	@Test
	public void decrAccount() {
		AccountService bean = DoradoBeanFactory.getBean(AccountService.class);
		bean.cost(110088, 100);
	}
	
}
