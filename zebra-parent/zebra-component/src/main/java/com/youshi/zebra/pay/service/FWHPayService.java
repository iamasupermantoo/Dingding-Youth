package com.youshi.zebra.pay.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.constants.InProduction;
import com.dorado.framework.crud.model.HasData;
import com.dorado.framework.tuple.TwoTuple;
import com.dorado.framework.utils.DoradoBeanFactory;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.dorado.mvc.reqcontext.Platform;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.github.phantomthief.util.ObjectMapperUtils;
import com.youshi.zebra.core.constants.config.ListConfigKey;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.service.CourseMetaService;
import com.youshi.zebra.exception.common.EntityNotNormalException;
import com.youshi.zebra.order.constants.OrderStatus;
import com.youshi.zebra.order.constants.PayStatus;
import com.youshi.zebra.order.model.OrderModel;
import com.youshi.zebra.order.service.OrderHandlerService;
import com.youshi.zebra.order.service.OrderService;
import com.youshi.zebra.pay.constants.PayChannel;
import com.youshi.zebra.pay.constants.WeixinPayConstants;
import com.youshi.zebra.pay.constants.WxpayTradeState;
import com.youshi.zebra.pay.dao.PayLogDAO;
import com.youshi.zebra.pay.dao.PayNotifyDAO;
import com.youshi.zebra.pay.exception.OrderNotFoundException;
import com.youshi.zebra.pay.utils.WxpayOrderStringBuilder;
import com.youshi.zebra.pay.utils.WxpayUtils;
import com.youshi.zebra.wx.component.wx.JSAPIPreorderBuilder;
import com.youshi.zebra.wx.component.wx.constants.WXFWHConstants;
import com.youshi.zebra.wx.component.wx.model.WxPayParams;

/**
 * 
 * 微信服务号支付, Service实现
 * 
 * 
 * @author wangsch
 * @date 2017年4月21日
 */
@Service
public class FWHPayService {
	private static final Logger logger = LoggerFactory.getLogger(FWHPayService.class);
	
	@Autowired
	private PayLogDAO payLogDAO;
	
	@Autowired
	private PayNotifyDAO payNotifyDAO;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private PayService payService;
	
	@Autowired
	private CourseMetaService courseMetaService;
	
	@Autowired
	private OrderHandlerService orderHandlerService;
	
	
	public String getQRCodeUrl(String orderSn, String body, int totalPrice) {
		String noncestr = RandomStringUtils.randomAlphanumeric(32);
		String codeUrl = WxpayOrderStringBuilder.builder()
				// 设置为扫码支付
				.setAppId(WXFWHConstants.WXPAY_APPID)
				.setMchId(WXFWHConstants.WXPAY_PARTNER_ID)
				.setApiSecret(WXFWHConstants.WXPAY_API_SECRET)
				.setNotifyUrl(WXFWHConstants.WX_WEB_PAY_NOTIFY_URL)
				.setTradeType("NATIVE")
				
				.setBody(body)
				.setNonceStr(noncestr)
				.setOutTradeNo(orderSn)
				.setTotalFee(String.valueOf(totalPrice))
				.setSpbillCreateIp(WebRequestContext.getCurrentIpInString())
				.buildCodeUrl();
		
		return codeUrl;
	}
	
	/**
	 * 获取支付参数, 生成订单
	 * 
	 */
	public TwoTuple<String, WxPayParams> getParams(Integer userId, String orderSn, String openId) {
		// 订单
		OrderModel order = orderService.getOrderBySn(orderSn);
		if(order == null || order.getOrderStatus() != OrderStatus.ADMIN_CONFIRMED.getValue()) {
			throw new EntityNotNormalException();
		}
		CourseMetaModel cm = courseMetaService.getById(order.getProductId());
		String body = cm.getName();
		
		int price = adjustPrice(userId, order.getTotalPrice());
		String totalFee = String.valueOf(price);
		
		long time = System.currentTimeMillis();
		PayChannel payChannel = PayChannel.WEIXIN_FWH;
		
		// 支付日志
		int paylogId = payLogDAO.insert(orderSn, PayStatus.NOT_PAID, payChannel, Platform.WX,
				HasData.EMPTY_DATA, time);
		logger.info("Paylog create succ. orderSn: {}, paylogId: {}", orderSn, paylogId);
		
		// 预支付id
		String nonceStr = RandomStringUtils.randomAlphanumeric(32);
		String prepayid = JSAPIPreorderBuilder.builder()
				.setBody(body)
				.setNonceStr(nonceStr)
				.setOutTradeNo(orderSn)
				.setTotalFee(totalFee)
				.setSpbillCreateIp(WebRequestContext.getCurrentIpInString())
//				.setAttach(attachBase64)
				.buildPrepayid(openId);
		
		// 支付参数
		nonceStr = RandomStringUtils.randomAlphanumeric(32);
		String timeStamp = String.valueOf(time / 1000);
		
		Map<String, String> payMap = new HashMap<>();
		payMap.put("appId", WXFWHConstants.APPID);
		payMap.put("timeStamp", timeStamp);
		payMap.put("nonceStr", nonceStr);
		payMap.put("package", "prepay_id=" + prepayid);
		payMap.put("signType", "MD5");
		String paySign = WxpayUtils.getSign(payMap, WXFWHConstants.WXPAY_API_SECRET);
		
		WxPayParams params = new WxPayParams(WXFWHConstants.APPID, 
				timeStamp, nonceStr, "prepay_id="+prepayid, "MD5", paySign);
		
		TwoTuple<String, WxPayParams> result = new TwoTuple<String, WxPayParams>(orderSn, params);
		logger.info("Params: {}", DoradoMapperUtils.toJSON(result));
		return result;
	}
	
	public PayStatus verifyResult(String externalUserId, String orderSn) {
		// 验证订单状态
		OrderModel order = orderService.getOrderBySn(orderSn);
		if(order == null) {
			logger.error("Verify fail, order Not found. orderSn: {}", orderSn);
			throw new OrderNotFoundException();
		}
		if(order.getOrderStatus() == OrderStatus.FINISHED.getValue()) {
			logger.info("Verify fail, order already finished. orderSn: {}", orderSn);
			return PayStatus.PAID;
		}
		
		// 查询支付状态
		PayStatus result = queryTrade(orderSn, null);;
		if(result != PayStatus.PAID) {
			logger.error("Verify fail, order NOT_PAID(this should NOT happen), orderSn: {}.", orderSn);
			return result;
		}
		
		return PayStatus.PAID;
	}
	
	
	
	
	@Test
	public void aaa() {
		FWHPayService bean = DoradoBeanFactory.getBean(FWHPayService.class);
		System.out.println(bean.verifyResult(null, "00201706201739515882"));
	}
	

	/**
	 * 
	 * 处理支付回调
	 * 
	 * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
	 * 
	 * @return 接收通知成功并校验成功, 返回true, 否则返回false
	 * 
	 */
	public boolean dealNotify(HttpServletRequest request) {
		Map<String, String> paramsMap = WxpayUtils.getXMLParamsFromRequest(request);
		boolean verifyOk = WxpayUtils.verifyNotify(paramsMap, WXFWHConstants.WXPAY_API_SECRET);
		if(!verifyOk) {
			logger.error("Verify WEIXIN FWH notify fail. notify params: {}", paramsMap);
			return false;
		}
		
		String orderSn = paramsMap.get("out_trade_no");
		OrderModel order = orderService.getOrderBySn(orderSn);
		if(order == null) {
			logger.error("Order Not found. orderSn: {}", orderSn);
			return false;
		}
		if(order.getOrderStatus() == OrderStatus.FINISHED.getValue()) {
			logger.info("Order already finished. orderSn: {}", orderSn);
			return true;
		}
		PayChannel payChannel = PayChannel.WEIXIN_FWH;
		
		long time = System.currentTimeMillis();
		int id = payNotifyDAO.insert(orderSn, payChannel, ObjectMapperUtils.toJSON(paramsMap), time);
		DAOUtils.checkInsert(id);
		logger.info("Insert pay notify succ. id: {}", id);
		
		String resultCode = paramsMap.get("result_code");
		// SUCCESS/FAIL
		if(!"SUCCESS".equals(resultCode)) {
			logger.error("Pay result code FAIL. notify params: {}", paramsMap);
			return false;
		}
		
		// 状态更改
		long currTime = System.currentTimeMillis();	// 当前时间
		int c = payLogDAO.update(orderSn, PayStatus.PAID, payChannel, 
				HasData.EMPTY_DATA, HasData.EMPTY_DATA, currTime);
		DAOUtils.checkAffectRows(c);
		logger.info("Update pay log status to PAID succ. orderSn: {}", orderSn);
		
		orderService.updateStatus(order.getId(), OrderStatus.FINISHED, PayStatus.PAID, payChannel, currTime);
		logger.info("Update order status to FINISHED and pay status to PAID succ. orderSn: {}", orderSn);
		
		// 业务处理
		orderHandlerService.paySucc(orderSn, payChannel);
		
		return true;
	}
	
	/**
	 * 
	 * 
	 * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_2
	 * 
	 * @param orderSn
	 * @param tradeid
	 * @return
	 */
	public static WxpayTradeState queryTradeState(String orderSn, String tradeid){
        Map<String, String> restmap = null;
        try {
            Map<String, String> parm = new HashMap<String, String>();
            parm.put("appid", WXFWHConstants.WXPAY_APPID);
            parm.put("mch_id", WXFWHConstants.WXPAY_PARTNER_ID);
//            parm.put("transaction_id", tradeid);
            parm.put("out_trade_no", orderSn);
            parm.put("nonce_str", RandomStringUtils.randomAlphanumeric(32));
            parm.put("sign", WxpayUtils.getSign(parm, WXFWHConstants.WXPAY_API_SECRET));

            restmap = WxpayUtils.restPrePayidXml(WeixinPayConstants.WXPAY_ORDER_QUERY,parm);
        } catch (Exception e) {
        	logger.error("Fail query weixin trade status.", e);
        	return null;
        }
        if(restmap == null) {
        	return null;
        }
        logger.info("Query weixin trade res: {}", ObjectMapperUtils.toJSON(restmap));
        
        String returnCode = restmap.get("return_code");
        if(!"SUCCESS".equals(returnCode)) {
        	return null;
        }
        
        String resultCode = restmap.get("result_code");
        if(!"SUCCESS".equals(resultCode)) {
        	return null;
        }
        
        // 返回交易状态
        return WxpayTradeState.valueOf(restmap.get("trade_state"));
    }
	
	public PayStatus queryTrade(String orderSn, String tradeId) {
		WxpayTradeState tradeState = queryTradeState(orderSn, tradeId);
		if(tradeState == WxpayTradeState.SUCCESS) {
			return PayStatus.PAID;
		}
		return PayStatus.NOT_PAID;
	}
	
	private static int adjustPrice(Integer userId, int price) {
		// 这里打个补丁，测试环境或者测试人员订单金额设置为1分钱
		if(InProduction.get()) {
			List<Object> userIds = ListConfigKey.PAY_TEST_USER_IDS.get();
			for (Object object : userIds) {
				Integer testUserId = (Integer)object;
				if(testUserId.intValue() == userId.intValue()) {
					price = 1;
					logger.info("Pay test userId found: {}, price set to 1.", testUserId);
					break;
				}
			}
		} else {
			price = 1;
		}
		return price;
	}
}
