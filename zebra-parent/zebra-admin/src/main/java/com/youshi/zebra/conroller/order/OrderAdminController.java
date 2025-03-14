package com.youshi.zebra.conroller.order;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.connect.service.ConnectService;
import com.youshi.zebra.order.constants.OrderStatus;
import com.youshi.zebra.order.model.OrderModel;
import com.youshi.zebra.order.service.OrderAdminService;
import com.youshi.zebra.order.service.OrderHandlerService;
import com.youshi.zebra.pay.constants.PayChannel;
import com.youshi.zebra.pay.service.PayService;
import com.youshi.zebra.view.OrderView;

import io.netty.util.internal.StringUtil;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * @author wangsch
 * @date 2017年2月17日
 */
@Controller
@RequestMapping(value = "/order/admin")
public class OrderAdminController {
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@Autowired
	private OrderAdminService orderAdminService;
	
	@Autowired
	private PayService payService;
	
	@Autowired
	private OrderHandlerService orderHandlerService;
	
	@Autowired
	@Qualifier("mobileConnectService")
	private ConnectService connectService;
	
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public ModelAndView list(
			@ApiIgnore @Visitor Integer adminId,
			@RequestParam(value = "mobile", required=false) String mobile,
//			@RequestParam(value = "status", required=false) OrderStatus status,
			@Uuid(value = "cursor", type=OrderModel.class, required = false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		Integer userId = parseUserId(mobile);
		OrderStatus status = OrderStatus.USER_COMMITED;
		PageView<OrderModel, HasUuid<Integer>> page = orderAdminService.queryOrder(userId, status, cursor, limit);
		
		Map<String, Object> dataMap = viewBuilder.buildToMap(page, "orders", 
				ExplicitViewMapper.getInstance().setViewClass(OrderView.class));
		ModelAndView mav = new ModelAndView("order/preorder_list").addAllObjects(dataMap);
		
		mav.addObject("mobile", mobile);
		return mav;
	}
	
	@RequestMapping(value = "/query", method=RequestMethod.GET)
	public ModelAndView orderlist(
			@ApiIgnore @Visitor Integer adminId,
			@RequestParam(value = "mobile", required=false) String mobile,
			@RequestParam(value = "status", required=false) OrderStatus status,
			@Uuid(value = "cursor", type=OrderModel.class, required = false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		Integer userId = parseUserId(mobile);
		
		PageView<OrderModel, HasUuid<Integer>> page = orderAdminService.queryOrder(userId, status, cursor, limit);
		
		Map<String, Object> dataMap = viewBuilder.buildToMap(page, "orders", 
				ExplicitViewMapper.getInstance().setViewClass(OrderView.class));
		ModelAndView mav = new ModelAndView("order/order_list").addAllObjects(dataMap);
		
		mav.addObject("mobile", mobile);
		
		return mav;
	}

	/**
	 * @param mobile
	 * @return
	 */
	private Integer parseUserId(String mobile) {
		Integer userId = null;
		if(StringUtils.isNotEmpty(mobile)) {
			userId = connectService.getUserIdByExternalUserId(mobile);
			if(userId == null) {
				userId = 0;
			}
		}
		return userId;
	}
	
	
	
	@RequestMapping(value = "/confirm", method=RequestMethod.POST)
	@ResponseBody
	public Object confirm(
			@Visitor Integer userId,
			@Uuid(value = "oid", type=OrderModel.class) Integer orderId,
			@RequestParam(value = "price") Integer price
			) {
		orderAdminService.confirmOrder(userId, orderId, price);
		
		return JsonResultView.SUCCESS;
	}
	
	@RequestMapping(value = "/chat/result", method=RequestMethod.POST)
	@ResponseBody
	public Object chatResult(
			@Visitor Integer userId,
			@Uuid(value = "oid", type=OrderModel.class) Integer orderId,
			@RequestParam(value = "result") Integer result,
			@RequestParam(value = "remark", required=false) String remark
			) {
		orderAdminService.chatResult(orderId, result, remark);
		
		return JsonResultView.SUCCESS;
	}
	
	@RequestMapping(value = "/paid", method=RequestMethod.POST)
	@ResponseBody
	public Object paid(
			@RequestParam(value = "orderSn") String orderSn
			) {
		orderHandlerService.paySucc(orderSn, PayChannel.ADMIN);
		
		return JsonResultView.SUCCESS;
	}
}
