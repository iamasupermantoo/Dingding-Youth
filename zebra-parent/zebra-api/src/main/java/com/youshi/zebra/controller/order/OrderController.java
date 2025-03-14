package com.youshi.zebra.controller.order;

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
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.order.model.OrderModel;
import com.youshi.zebra.order.service.OrderService;
import com.youshi.zebra.order.service.PreOrderService;
import com.youshi.zebra.view.OrderView;

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
	private PreOrderService preOrderService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@ApiOperation(value = "提交订单", tags=SwaggerTags.ORDER_PAY)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "商品id（课程id）", name="product", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public Object submit(
			@ApiIgnore @Visitor Integer userId,
			@RequestParam(value = "product") String productUuid,
			@RequestParam(value = "type") Integer type
			) {
		OrderModel order = orderService.submit(userId, productUuid, type);
		return new JsonResultView().addValue("orderSn", order.getOrderSn())
				.addValue("oid", order.getUuid());
	}
	
	
	@ApiOperation(value = "立即约课", tags=SwaggerTags.ORDER_PAY)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "商品id（课程id）", name="product", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/commit", method=RequestMethod.POST)
	public Object commit(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(type = CourseMetaModel.class, value = "product") Integer cmId
			) {
		orderService.commit(userId, cmId);
		
		return JsonResultView.SUCCESS;
	}
	
	@ApiOperation(value = "订单列表", tags=SwaggerTags.ORDER_PAY)
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
