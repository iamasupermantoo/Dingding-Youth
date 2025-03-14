package com.youshi.zebra.pay.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.youshi.zebra.core.utils.HttpClientHolder;
import com.youshi.zebra.exception.base.DoradoRuntimeException;
import com.youshi.zebra.pay.constants.WeixinPayConstants;
import com.youshi.zebra.pay.constants.WxpayTradeState;

/**
 * 
 * @author wangsch
 * @date 2017年2月6日
 */
public class WxpayUtils {
    private static final Logger logger = LoggerFactory.getLogger(WeixinPayConstants.LOGGER_NAME);

    public static String getSign(Map<String, String> params, String paternerKey) {
        return DigestUtils.md5Hex(createSign(params, false) + "&key=" + paternerKey).toUpperCase();
    }

    
    public static void payQRCode(String filePath, String fileName, int width, int height, String format, 
			String codeUrl) {
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix;
		try {
			bitMatrix = new MultiFormatWriter().encode(codeUrl, BarcodeFormat.QR_CODE, 
					width, height, hints);
			Path path = FileSystems.getDefault().getPath(filePath, fileName);
			MatrixToImageWriter.writeToPath(bitMatrix, format, path);
		} catch (Exception e) {
			throw new DoradoRuntimeException(e);
		}
	}
    
    
    public static BufferedImage payQRCode(int width, int height, 
			String codeUrl) {
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix;
		try {
			bitMatrix = new MultiFormatWriter().encode(codeUrl, BarcodeFormat.QR_CODE, 
					width, height, hints);
			return MatrixToImageWriter.toBufferedImage(bitMatrix);
		} catch (Exception e) {
			throw new DoradoRuntimeException(e);
		}
	}
    
    
    public static String createSign(Map<String, String> params, boolean encode) {
        Set<String> keysSet = params.keySet();
        Object[] keys = keysSet.toArray();
        Arrays.sort(keys);
        StringBuffer result = new StringBuffer();
        boolean first = true;
        for (Object key : keys) {
            if (key == null || StringUtils.isEmpty(params.get(key))) // 参数为空不参与签名
                continue;
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(key).append("=");
            Object value = params.get(key);
            String valueStr = "";
            if (null != value) {
                valueStr = value.toString();
            }
            if (encode) {
                try {
					result.append(URLEncoder.encode(valueStr, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// impossible
				}
            } else {
                result.append(valueStr);
            }
        }
        return result.toString();
    }

    //将封装好的参数转换成Xml格式类型的字符串
    public static String getRequestXml(Map<String,String> parameters){
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            if("sign".equalsIgnoreCase(k)){

            }else if ("attach".equalsIgnoreCase(k)||"body".equalsIgnoreCase(k)) {
                sb.append("<"+k+">"+"<![CDATA["+v+"]]></"+k+">");
            }else {
                sb.append("<"+k+">"+v+"</"+k+">");
            }
        }
        sb.append("<"+"sign"+">"+"<![CDATA["+parameters.get("sign")+"]]></"+"sign"+">");
        sb.append("</xml>");
        return sb.toString();
    }

    public static Map readStringXmlOut(String strxml){
        Map<String,String> map = new HashMap<String,String>();
        Document doc = null;
        if(StringUtils.isNotEmpty(strxml)){
            try {
                doc = DocumentHelper.parseText(strxml); // 将字符串转为XML
                Element rootElt = doc.getRootElement(); // 获取根节点
                List<Element> list = rootElt.elements();//获取根节点下所有节点
                for (Element element : list) {  //遍历节点
                    map.put(element.getName(), element.getText()); //节点的name为map的key，text为map的value
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 发送请求
     * 
     * @param url
     * @param data
     * @return
     */
    public static Map<String, String> restPrePayidXml(String url,Map<String, String> data){
    	logger.info("url: {}, data: {}", url, data);
        HttpClient httpClient = HttpClientHolder.getHttpClient();
        String responseText = "";
        HttpResponse httpResponse = null;

        try {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().
                    setSocketTimeout(5000).
                    setConnectTimeout(5000).
                    build();
            httpPost.setConfig(requestConfig);

            httpPost.setEntity(new StringEntity(getRequestXml(data), "UTF-8"));

            httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == 200) {
                responseText = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            } else {
            	logger.error("query fail: {}", statusCode);
            }
        } catch (Exception e) {
            logger.error("", e);
        }

        logger.info("xml: {}", responseText);
        
        return readStringXmlOut(responseText);
    }

    public static boolean verifyNotify(Map<String, String> restmap){
        return verifyNotify(restmap, WeixinPayConstants.WXPAY_API_SECRET);
    }
    
    public static boolean verifyNotify(Map<String, String> restmap, String apiSecret){
        boolean isOK = false;
        try {
            String sing = restmap.get("sign"); // 返回的签名
            restmap.remove("sign");
            String signnow = getSign(restmap, apiSecret);
            if (signnow.equals(sing)) {
                isOK = true;
            }
        } catch (Exception e) {
        }

        return isOK;
    }

    /**
     * 订单查询
     * @param tradeid
     * @param tradeno
     */
    public static WxpayTradeState orderPayQuery(String tradeid, String tradeno){
    	return orderPayQuery(tradeid, tradeno, 
    			WeixinPayConstants.APP_ID,  WeixinPayConstants.PARTNER_ID, WeixinPayConstants.WXPAY_API_SECRET);
    }
    
    public static WxpayTradeState orderPayQuery(String tradeid, String tradeno, 
    		String appId, String partnerId, String apiSecret){
        Map<String, String> restmap = null;
        try {
            Map<String, String> parm = new HashMap<String, String>();
            parm.put("appid", appId);
            parm.put("mch_id", partnerId);
//            parm.put("transaction_id", tradeid);
            parm.put("out_trade_no", tradeno);
            parm.put("nonce_str", RandomStringUtils.randomAlphanumeric(32));
            parm.put("sign", WxpayUtils.getSign(parm, apiSecret));

            restmap = restPrePayidXml(WeixinPayConstants.WXPAY_ORDER_QUERY,parm);
        } catch (Exception e) {
        }

        //return_code如果是SUCCESS的话，此字段只表示是通信标识，非交易标识，交易是否成功需要查看trade_state来判断
        if (restmap.size() > 0 && "SUCCESS".equals(restmap.get("result_code"))) {
            // 订单查询成功 处理业务逻辑
        	return WxpayTradeState.valueOf(restmap.get("trade_state"));
        }
        return null;
    }
    
    
    
    

    public static Map<String, String> getXMLParamsFromRequest(HttpServletRequest request){
        Map<String, String> restmap = null;
        try {
            ServletInputStream in = request.getInputStream();
            String resxml = readInputStream2String(in);
            if(StringUtils.isNotEmpty(resxml)){
                restmap = readStringXmlOut(resxml);
            }
        } catch (Exception e) {
        }
        return restmap;
    }

    public static final String readInputStream2String(InputStream in) {
        if (in == null)
            return "";
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[1024];
        try {
            for (int n; (n = in.read(b)) != -1;) {
                out.append(new String(b, 0, n, "UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(in != null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return out.toString();
    }
}
