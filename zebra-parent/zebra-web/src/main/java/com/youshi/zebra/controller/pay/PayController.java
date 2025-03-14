package com.youshi.zebra.controller.pay;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.core.web.annotation.LoginRequired;
import com.youshi.zebra.exception.base.DoradoRuntimeException;
import com.youshi.zebra.order.constants.PayStatus;
import com.youshi.zebra.pay.constants.AliWebPayConstants;
import com.youshi.zebra.pay.constants.PayChannel;
import com.youshi.zebra.pay.service.WebPayService;
import com.youshi.zebra.pay.utils.WxpayUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * Web支付相关
 * 
 * @author wangsch
 * @date 2017年4月18日
 */
@RequestMapping(value = "/pay")
@Controller
public class PayController {
	private static final Logger logger = LoggerFactory.getLogger(PayController.class);
	
	@Autowired
	private WebPayService webPayService;
	
	@ApiOperation(value = "获取微信支付，二维码图片地址", tags=SwaggerTags.ORDER_PAY)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/wx/req", method=RequestMethod.GET)
	@LoginRequired
	public void wxReq(
			@ApiIgnore @Visitor Integer userId,
			@ApiParam(value = "订单编号", required=true)
			@RequestParam(value = "orderSn") String orderSn,
			HttpServletResponse resp
			) {
		BufferedImage qrCode = webPayService.getQRCode(userId, orderSn);
		outputQrCode(resp, qrCode);
	}
	
	@ApiOperation(value = "获取支付宝收银台链接", tags=SwaggerTags.ORDER_PAY)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/ali/req", method=RequestMethod.GET)
	@LoginRequired
	public void aliReq(
			@ApiIgnore @Visitor Integer userId,
			@ApiParam(value = "订单编号", required=true)
			@RequestParam(value = "orderSn") String orderSn,
			HttpServletResponse response
			) {
		String form = webPayService.form(userId, orderSn);
		try {
			response.setContentType("text/html;charset=" + AliWebPayConstants.CHARSET);
			response.getWriter().write(form);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			throw new DoradoRuntimeException();
		}
	}
	
	@ApiOperation(value = "验证支付结果。0: 未支付，1:已支付", tags=SwaggerTags.ORDER_PAY)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", dataType="String", required=true, paramType="query"),
	})
	@RequestMapping(value = "/verify", method=RequestMethod.POST)
	@LoginRequired
	@ResponseBody
	public Object result(
			@ApiIgnore @Visitor Integer userId,
			@ApiParam(value = "订单编号", required=true)
			@RequestParam(value = "orderSn") String orderSn,
			
			@ApiParam(value = "支付渠道。0:微信，1:支付宝", required=true)
			@RequestParam(value = "channel") Integer payChannel
			) {
		PayStatus payStatus = webPayService.verifyResult(userId, payChannel, orderSn, null, null);
		
		return new JsonResultView().addValue("result", payStatus.getValue());
	}
	
	/**
	 * 支付宝异步通知，回调接口。
	 * 
	 * 
	 */
	@ApiIgnore
	@RequestMapping(value = "/ali/notify", method=RequestMethod.POST)
	public String aliNotify(
			HttpServletRequest request, HttpServletResponse response) {
		boolean succ = webPayService.dealNotify(request, PayChannel.ALIPAY);
		return succ ? "success" : "fail";
	}
	
	@ApiIgnore
	@RequestMapping(value = "/wx/notify", method=RequestMethod.POST)
	public void weixinNotify(HttpServletRequest request,  HttpServletResponse response) throws Exception{
		boolean succ = webPayService.dealNotify(request, PayChannel.WEIXIN);
		
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

	/**
	 * @param resp
	 * @param qrCode
	 */
	private void outputQrCode(HttpServletResponse resp, BufferedImage qrCode) {
		try {
			resp.setContentType("image/png");
			ServletOutputStream outputStream = resp.getOutputStream();
			ImageIO.write(qrCode, "png", outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			logger.error("Fail output picture code.", e);
		}
	}
}
