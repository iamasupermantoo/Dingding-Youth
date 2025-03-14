package com.youshi.zebra.conroller.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.account.model.AccountChargeItemModel;
import com.youshi.zebra.account.service.AccountService;
import com.youshi.zebra.order.model.OrderModel;
import com.youshi.zebra.order.service.ProductAdminService;
import com.youshi.zebra.order.service.ProductService;
import com.youshi.zebra.view.AccountChargeItemView;

import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * @author wangsch
 * @date 2017年2月17日
 */
@RestController
@RequestMapping(value = "/product/admin")
public class ProductAdminController {
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductAdminService productAdminService;
	
	@Autowired
	private AccountService accountService;
	
	@RequestMapping(value = "/charge/item/list", method=RequestMethod.GET)
	public ModelAndView list(
			@ApiIgnore @Visitor Integer adminId,
			@Uuid(value = "cursor", type=OrderModel.class, required = false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		List<AccountChargeItemModel> chargeItems = accountService.getChargeItems();
		
		List<Object> items = viewBuilder.buildToList(chargeItems, 
				ExplicitViewMapper.getInstance().setViewClass(AccountChargeItemView.class));
		
		ModelAndView mav = new ModelAndView("order/charge_item_list")
				.addObject("items", items);
		return mav;
	}
	
	@RequestMapping(value = "/charge/item/add", method=RequestMethod.POST)
	public Object addChargeItem(
			@RequestParam(value = "price") Integer price,
			@RequestParam(value = "plusAmount") Integer plusAmount,
			@RequestParam(value = "appleProductId") String appleProductId
			) {
		price = price * 100; // 元转换为分
		
		Integer originalPrice = price;
		productAdminService.createChargeItem(appleProductId, price, originalPrice, plusAmount);
		return JsonResultView.SUCCESS;
	}
	
	@RequestMapping(value = "/charge/item/remove", method=RequestMethod.POST)
	public Object removeChargeItem(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "id", type=AccountChargeItemModel.class) Integer chargeItemId
			) {
		productAdminService.removeChargeItem(userId, chargeItemId);
		return JsonResultView.SUCCESS;
	}
}
