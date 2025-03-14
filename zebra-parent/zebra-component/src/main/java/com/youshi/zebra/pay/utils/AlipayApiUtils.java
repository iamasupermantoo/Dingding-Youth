package com.youshi.zebra.pay.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.util.HttpURLConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.github.phantomthief.util.ObjectMapperUtils;
import com.google.common.collect.ImmutableMap;
import com.youshi.zebra.pay.constants.AlipayConstants;
import com.youshi.zebra.pay.exception.AlipayCallException;

/**
 * 
 * 支付宝API，工具类
 * 
 * 
 * @author wangsch
 * @date 2017年2月4日
 */
public class AlipayApiUtils {
	private static final Logger logger = LoggerFactory.getLogger(AlipayConstants.LOGGER_NAME); 
	
	private static final AlipayClient client = new DefaultAlipayClient(AlipayConstants.ALIPAY_GATEWAY, 
			AlipayConstants.APP_ID, AlipayConstants.APP_PRIVATE_KEY, AlipayConstants.FORMAT, 
			AlipayConstants.CHARSET, AlipayConstants.ALIPAY_PUBLIC_KEY, AlipayConstants.SIGN_TYPE);
	
	/**
	 * 验证支付宝的回调请求
	 * 
	 * @param request	原始http请求
	 * @return			成功返回true，否则返回false
	 * 
	 * @throws AlipayApiException
	 */
	public static final boolean verifyNotify(Map<String, String> paramsMap) {
		return verifyNotify(paramsMap, AlipayConstants.ALIPAY_PUBLIC_KEY);
	}
	
	public static final boolean verifyNotify(Map<String, String> paramsMap, String aliPublicKey) {
		boolean succ = false;
		try {
			String sign = paramsMap.get("sign");
			succ = AlipaySignature.rsa256CheckContent(AlipaySignature.getSignCheckContentV1(paramsMap), sign,
					aliPublicKey, AlipayConstants.CHARSET);
		} catch (AlipayApiException e) {
			AlipayCallException ex = dealAlipayApiException(e);
			throw ex;
		}
		
		return succ;
	}
	
	public static boolean verify(Map<String, String> paramsMap) {
		boolean signVerified = false;
		try {
			signVerified = AlipaySignature.rsaCheckV1(paramsMap, 
					AlipayConstants.ALIPAY_PUBLIC_KEY, AlipayConstants.CHARSET);
		} catch (AlipayApiException e) {
			AlipayCallException ex = dealAlipayApiException(e);
			throw ex;
		}
//		if(signVerified){
//		   // TODO 验签成功后
//		   //按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure
//		} else{
//		    // TODO 验签失败则记录异常日志，并在response中返回failure.
//		}
		
		return signVerified;
		
	}
	
	/**
	 * 支付宝交易状态查询
	 * 
	 * <a href="https://doc.open.alipay.com/doc2/apiDetail.htm?spm=a219a.7629065.0.0.PlTwKb&apiId=757&docType=4">统一收单线下交易查询</a>
	 * 
	 * @param outTradeNo	商户订单号
	 * @param tradeNo		支付宝交易号，和 {@code outTradeNo}不能同时为空，同时存在时，优先使用 {@code tradeNo}
	 * 
	 * @return 				{@link AlipayTradeQueryResponse}不会返回null
	 */
	public static final AlipayTradeQueryResponse tradeQuery(String outTradeNo, String tradeNo) {
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		
		String bizContent = ObjectMapperUtils.toJSON(ImmutableMap.<String, String>builder()
				.put("out_trade_no", outTradeNo)
//				.put("trade_no", tradeNo)
				.build());
		
		request.setBizContent(bizContent);
		AlipayTradeQueryResponse response = null;
		try {
			response = client.execute(request);
		} catch (AlipayApiException e) {
			AlipayCallException ex = dealAlipayApiException(e);
			throw ex;
		}
		/*
		 *  AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do","app_id","your private_key","json","GBK","alipay_public_key","RSA2");
			AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
			request.setBizContent("{" +
			"    \"out_trade_no\":\"20150320010101001\"," +
			"    \"trade_no\":\"2014112611001004680 073956707\"" +
			"  }");
			AlipayTradeQueryResponse response = alipayClient.execute(request);
			if(response.isSuccess()){
			System.out.println("调用成功");
			} else {
			System.out.println("调用失败");
			}
		 * 
		 * 
		 */
		
		return response;
	}

	/**
	 * 处理{@link AlipayApiException}异常（检查型异常），返回为{@link AlipayCallException}异常（非检查型异常）
	 * 
	 * @param e
	 * @return
	 */
	public static AlipayCallException dealAlipayApiException(AlipayApiException e) {
		AlipayCallException ex = new AlipayCallException();
		ex.setReason(e.getErrMsg());
		logger.error("Alipay api call fail. code: " + e.getErrCode() +", msg: " + e.getErrMsg(), e);
		return ex;
	}
	
	/**
	 * 支付宝查询对账单下载地址
	 * 
	 * <a href="https://doc.open.alipay.com/doc2/apiDetail.htm?spm=a219a.7629065.0.0.iX5mPA&apiId=1054&docType=4">查询对账单下载地址</a>
	 * 
	 * @param type				账单类型
	 * @param date				账单时间
	 * @return					成功返回账单下载地址，否则返回null
	 */
	public static final String billUrl(String type, String date) {
		AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
		
		String bizContent = ObjectMapperUtils.toJSON(ImmutableMap.<String, String>builder()
				.put("bill_type", type)
				.put("bill_date", date)
				.build());
		
		request.setBizContent(bizContent);
		AlipayDataDataserviceBillDownloadurlQueryResponse response = null;
		try {
			response = client.execute(request);
		} catch (AlipayApiException e) {
			AlipayCallException ex = dealAlipayApiException(e);
			throw ex;
		}
		String url = response.isSuccess() ? response.getBillDownloadUrl() : null;
		return url;
	}
	
	/**
	 * 下载账单到本地 OPTI TODO 失败抛异常
	 * 
	 * @param urlStr			下载地址url
	 * @param saveToFile		账单保存路径，绝对路径。如：/root/bills/alipay_bill_2017-11-11.csv
	 */
	public static final void downloadBill(String urlStr, String saveToFile) {
		URL url = null;
		HttpURLConnection httpUrlConnection = null;
		InputStream fis = null;
		FileOutputStream fos = null;
		try {
		    url = new URL(urlStr);
		    httpUrlConnection = (HttpURLConnection) url.openConnection();
		    httpUrlConnection.setConnectTimeout(5 * 1000);
		    httpUrlConnection.setDoInput(true);
		    httpUrlConnection.setDoOutput(true);
		    httpUrlConnection.setUseCaches(false);
		    httpUrlConnection.setRequestMethod("GET");
		    httpUrlConnection.setRequestProperty("Charsert", "UTF-8");
		    httpUrlConnection.connect();
		    fis = httpUrlConnection.getInputStream();
		    byte[] temp = new byte[1024];
		    int b;
		    fos = new FileOutputStream(new File(saveToFile));
		    while ((b = fis.read(temp)) != -1) {
		        fos.write(temp, 0, b);
		        fos.flush();
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
		    try {
		        fis.close();
		        fos.close();
		        httpUrlConnection.disconnect();
		    } catch (NullPointerException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public static final Map<String, String> singleParamMap(HttpServletRequest request) {
		Map<String, String[]> params = request.getParameterMap();
		Map<String, String> result = new HashMap<>(params.size());
		
		for (Entry<String, String[]> entry : params.entrySet()) {
			result.put(entry.getKey(), entry.getValue()[0]);
		}
		
		return result;
	}
	
	
}
