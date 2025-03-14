package com.youshi.zebra.pay.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.youshi.zebra.core.exception.EntityNotFoundException;
import com.youshi.zebra.order.constants.PayStatus;
import com.youshi.zebra.order.constants.ProductType;
import com.youshi.zebra.order.model.ProductPriceModel;
import com.youshi.zebra.order.service.ProductService;
import com.youshi.zebra.pay.model.ApplePayParams;

/**
 * 
 * 苹果支付Service
 * 
 * @author wangsch
 * @date 2017年4月17日
 */
@Service
public class ApplePayService {
	private static final Logger logger = LoggerFactory.getLogger(ApplePayService.class);
	
	@Autowired
	private ProductService productService;
	
	/**
	 * 
	 * 获取客户端唤起苹果支付需要的参数
	 */
	public ApplePayParams getParams(Integer userId, String orderSn, 
			Integer productId, ProductType productType) {
		ProductPriceModel productPrice = productService.getProductPrice(productId, productType);
		if(productPrice == null) {
			logger.error("Can't find apple product. userId: {}, productId: {}, productType: {}", 
					userId, productId, productType);
			throw new EntityNotFoundException();
		}
		
		ApplePayParams result = new ApplePayParams(productPrice.getAppleProductId());
		return result;
	}
	
	public PayStatus verify(String orderSn, String data) {
//		String dataDecoded = null;
//		try {
//			dataDecoded = URLDecoder.decode(data, "utf-8");
//		} catch (UnsupportedEncodingException e) {
//			// ignore
//		}

//		logger.info("Apple verify params. data: {}, dataDecoded: {}, orderSn: {}", 
//				data, dataDecoded, orderSn);
		
		JSONObject result = verifyReceipt1(data);
		logger.info("Apple verify params. data: {}, orderSn: {}", 
				data, orderSn);
		logger.info("支付结果:" + result.toJSONString());
		Integer status = result.getInteger("status");
		if (status != null && status == 0) {
//			JSONObject inApp = (JSONObject) result.getJSONObject("receipt").getJSONArray("in_app").get(0);
//			int ts = (int) (inApp.getLong("purchase_date_ms") / 1000);
//			rechargeService.applePay(loginId, inApp.get("product_id").toString(),
//					inApp.get("transaction_id").toString(), ts, liveid);
			
			logger.info("Ok here");
			return PayStatus.PAID;
		} else {
			return PayStatus.NOT_PAID;
		}
	}
	
	private static JSONObject verifyReceipt1(String recepit) {
		return verifyReceipt1("https://buy.itunes.apple.com/verifyReceipt", recepit);
	}

	private static JSONObject verifyReceipt1(String url, String receipt) {
		try {
			HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setAllowUserInteraction(false);
			PrintStream ps = new PrintStream(connection.getOutputStream());
			ps.print("{\"receipt-data\": \"" + receipt + "\"}");
			ps.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String str;
			StringBuffer sb = new StringBuffer();
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			br.close();
			String resultStr = sb.toString();
			JSONObject result = JSONObject.parseObject(resultStr);
			logger.info("Apple pay return result:{} ", result);
			if (result != null && result.getInteger("status") == 21007) {
				// 苹果在审核阶段仍然走测试接口，所以如果返回21007，则需要再次提交测试接口
				logger.info("Apple pay return status 21007. ");
				return verifyReceipt1("https://sandbox.itunes.apple.com/verifyReceipt", receipt);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
