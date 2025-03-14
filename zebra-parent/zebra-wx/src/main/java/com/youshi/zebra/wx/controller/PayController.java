package com.youshi.zebra.wx.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.framework.tuple.TwoTuple;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.order.constants.PayStatus;
import com.youshi.zebra.pay.model.PayParams;
import com.youshi.zebra.pay.service.FWHPayService;
import com.youshi.zebra.pay.service.PayService;
import com.youshi.zebra.pay.utils.WxpayUtils;
import com.youshi.zebra.wx.component.wx.model.WxPayParams;
import com.youshi.zebra.wx.component.wx.utils.WXUtils;
import com.youshi.zebra.wx.configuration.Swagger2Configuration.SwaggerTags;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * 支付相关。如：支付、余额、押金、充值记录等
 * 
 * @author wangsch
 * @date 2017年7月17日
 */
@RestController
@RequestMapping(value = "/wxPay")
public class PayController {

	@Autowired
	private PayService payService;
	
	@Autowired
	private FWHPayService fwhPayService;
	
	@ApiOperation(value = "获取唤起客户端需要的参数", tags=SwaggerTags.ORDER_PAY, response=PayParams.class)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/params", method={RequestMethod.GET, RequestMethod.POST})
	public Object params(
			@ApiIgnore @Visitor Integer userId,
			@ApiParam(value = "订单编号", required=true)
			@RequestParam(value = "orderSn") String orderSn
			) {
		String externalUserId = WXUtils.getExternalUserId();
//		String externalUserId = "oMfz0wzhlJAo2B1jIm1omRfR71-I";
		if(StringUtils.isEmpty(externalUserId)) {
			return new JsonResultView(ZebraMetaCode.Forbidden);
		}
		TwoTuple<String, WxPayParams> tuple = fwhPayService.getParams(userId, orderSn, externalUserId);
		WxPayParams params = tuple.second;
		return new JsonResultView().addValue("weiXin", params)
				.addValue("orderSn", tuple.first);
	
	}
	
	
	/**
	 * 验证支付结果
	 */
	@ApiOperation(value = "验证支付结果。0: 未支付，1:已支付", tags=SwaggerTags.ORDER_PAY)
	@RequestMapping(value = "/verify", method=RequestMethod.POST)
	public Object verify(
			@RequestParam(value = "orderSn") String orderSn
			) {
		String externalUserId = WXUtils.getExternalUserId();
		if(StringUtils.isEmpty(externalUserId)) {
			return new JsonResultView(ZebraMetaCode.Forbidden);
		}
		
		PayStatus payStatus = fwhPayService.verifyResult(externalUserId, orderSn);
		
		return new JsonResultView().addValue("result", payStatus.getValue());
	}
	
	@ApiIgnore
	@RequestMapping(value = "/notify", method=RequestMethod.POST)
	public void weixinNotify(HttpServletRequest request,  HttpServletResponse response) throws Exception{
		boolean succ = fwhPayService.dealNotify(request);
		
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
