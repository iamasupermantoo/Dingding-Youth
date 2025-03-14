package com.youshi.zebra.conroller.finance;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.order.constants.OrderStatus;
import com.youshi.zebra.order.model.OrderModel;
import com.youshi.zebra.order.service.OrderAdminService;
import com.youshi.zebra.view.OrderView;

import springfox.documentation.annotations.ApiIgnore;


@Controller
@RequestMapping(value = "/finance/admin")
public class FinanceAdminController {
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@Autowired
	private OrderAdminService orderAdminService;
	
	@RequestMapping(value = "/query", method=RequestMethod.GET)
	public ModelAndView orderlist(
			@ApiIgnore @Visitor Integer userId,
			@Uuid(value = "cursor", type=OrderModel.class, required = false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<OrderModel, HasUuid<Integer>> page = orderAdminService.queryOrder(userId, OrderStatus.FINISHED, cursor, limit);
		
		Map<String, Object> dataMap = viewBuilder.buildToMap(page, "orders", 
				ExplicitViewMapper.getInstance().setViewClass(OrderView.class));
		return new ModelAndView("finance/finance_list").addAllObjects(dataMap);
	}
}
