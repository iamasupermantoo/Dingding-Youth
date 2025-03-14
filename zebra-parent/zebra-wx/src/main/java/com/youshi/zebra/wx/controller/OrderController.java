package com.youshi.zebra.wx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.core.web.annotation.LoginRequired;
import com.youshi.zebra.order.model.OrderModel;
import com.youshi.zebra.order.service.OrderService;
import com.youshi.zebra.wx.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.wx.view.OrderView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * @author wangsch
 * @date 2017年6月12日
 */
@RequestMapping(value = "/order")
@RestController
public class OrderController {
	
	@Autowired
	private OrderService orderService;

	
	@Autowired
	private ViewBuilder viewBuilder;
	
	
	@ApiOperation(value = "订单列表", tags=SwaggerTags.ORDER_PAY, response=OrderView.class)
	@LoginRequired
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "cursor", name="cursor", dataType="String", required=false, paramType="query"),
	})
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public Object list(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "cursor", type = OrderModel.class, required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<OrderModel, HasUuid<Integer>> pageView = orderService.getOrders(userId, cursor, limit);
		JsonResultView result = viewBuilder.build(pageView, "orders", 
				ExplicitViewMapper.getInstance().setViewClass(OrderView.class));
		
		return result;
	}
	
	
}
