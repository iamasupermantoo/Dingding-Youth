package com.youshi.zebra.controller.pay;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.reqcontext.Platform;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.youshi.zebra.account.model.AccountChargeItemModel;
import com.youshi.zebra.account.service.AccountService;
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.core.web.annotation.LoginRequired;
import com.youshi.zebra.exception.common.ForbiddenException;
import com.youshi.zebra.order.constants.PayStatus;
import com.youshi.zebra.order.model.OrderModel;
import com.youshi.zebra.pay.constants.PayChannel;
import com.youshi.zebra.pay.model.PayParams;
import com.youshi.zebra.pay.service.PayService;
import com.youshi.zebra.pay.utils.WxpayUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * 支付相关
 * 
 * @author wangsch
 * @date 2017年4月18日
 */
@RequestMapping(value = "/pay")
@RestController
public class PayController {
	@Autowired
	private PayService payService;
	
	@Autowired
	private AccountService accountService;
	
	@ApiOperation(value = "获取充值项", tags=SwaggerTags.ORDER_PAY)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/charge/items", method=RequestMethod.GET)
	@LoginRequired
	public Object charge(
			@ApiIgnore @Visitor Integer userId
			) {
		List<AccountChargeItemModel> chargeItems = accountService.getChargeItems();
		Integer totalAmount = accountService.getUserTotalAmount(userId);
		
		JsonResultView result = new JsonResultView();
		
		List<ChargeItemView> items = chargeItems.stream().map(ChargeItemView::new).collect(Collectors.toList());
		result.addValue("items", items);
		result.addValue("totalAmount", totalAmount);
		
		return result;
	}
	
	public class ChargeItemView {
		private AccountChargeItemModel delegate;
		
		public ChargeItemView(AccountChargeItemModel delegate) {
			this.delegate = delegate;
		}
		
		public String getProduct() {
			return UuidUtils.getUuid(AccountChargeItemModel.class, delegate.getId());
		}
		
		public String getPrice() {
			return delegate.getPrice() / 100 + "";
		}
		
		public String getPlusAmount() {
			return delegate.getPlusAmount() + "";
		}
	}
	
	@ApiOperation(value = "用余额支付", tags=SwaggerTags.ORDER_PAY)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "商品id（课程id）", name="product", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/cost", method = RequestMethod.POST)
	@LoginRequired
	public Object cost(
			@ApiIgnore @Visitor Integer userId,
			@ApiParam(value = "商品id", required=true)
			@RequestParam(value = "product") String productUuid,
			@ApiParam(value = "商品类型", required=true)
			@RequestParam(value = "type") Integer type
			) {
		accountService.cost(userId, productUuid, type);
		
		return JsonResultView.SUCCESS;
	}
	
	@ApiOperation(value = "获取唤起客户端需要的参数", tags=SwaggerTags.ORDER_PAY, response=PayParams.class)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "订单id", name="oid", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/params", method=RequestMethod.POST)
	@LoginRequired
	public Object params(
			@ApiIgnore @Visitor Integer userId,
			@ApiParam(value = "订单id", required=true)
			@ApiIgnore @Uuid(value = "oid", type=OrderModel.class) Integer orderId,
			@ApiParam(value = "支付渠道。0:微信，1:支付宝，2:苹果支付", required=true)
			@RequestParam(value = "channel") Integer payChannel
			) {
		Platform platform = WebRequestContext.getAppPlatform();
		if(platform == null) {
			throw new ForbiddenException();
		}
		PayParams params = payService.getParams(userId, orderId, payChannel, platform);
		return new JsonResultView().addValue("params", params);
	}
	
	@ApiOperation(value = "验证支付结果。0: 未支付，1:已支付", tags=SwaggerTags.ORDER_PAY)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/verify", method=RequestMethod.POST)
	@LoginRequired
	public Object result(
			@ApiIgnore @Visitor Integer userId,
			@ApiParam(value = "订单编号", required=true)
			@RequestParam(value = "orderSn") String orderSn,
			
			@ApiParam(value = "支付渠道。0:微信，1:支付宝，2:苹果支付", required=true)
			@RequestParam(value = "channel") Integer payChannel,
			
			@ApiParam(value = "苹果支付时，必填")
			@RequestParam(value = "data", required=false) String data,
			
			@ApiParam(value="微信或支付宝时，必填，代表交易id")
			@RequestParam(value = "trade",required = false) String tradeId
			) {
		PayStatus payStatus = payService.verifyResult(userId, payChannel, orderSn, data, tradeId);
		
		return new JsonResultView().addValue("result", payStatus.getValue());
	}
	
	/**
	 * 支付宝异步通知，回调接口。
	 * 
	 * <a href="https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.BN3jdy&treeId=204&articleId=105301&docType=1">
	 * App支付结果异步通知</a>
	 * 
	 */
	@ApiIgnore
	@RequestMapping(value = "/ali/notify", method=RequestMethod.POST)
	public String aliNotify(
			HttpServletRequest request, HttpServletResponse response) {
		boolean succ = payService.dealNotify(request, PayChannel.ALIPAY);
		return succ ? "success" : "fail"; 
	}
	
	@ApiIgnore
	@RequestMapping(value = "/wx/notify", method=RequestMethod.POST)
	public void weixinNotify(HttpServletRequest request,  HttpServletResponse response) throws Exception{
		boolean succ = payService.dealNotify(request, PayChannel.WEIXIN);
		
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-type","text/html;charset=UTF-8");
		PrintWriter writer = response.getWriter();

		if(succ){
			// 处理成功后相应给响应xml
			Map<String, String> respMap = new HashMap<String, String>();
			respMap = new HashMap<String, String>();
			respMap.put("return_code", "SUCCESS");  //相应给微信服务器
			respMap.put("return_msg", "OK");
			String resXml = WxpayUtils.getRequestXml(respMap);
			writer.write(resXml);
		}else {
			writer.write("fail");
		}
	}
}
