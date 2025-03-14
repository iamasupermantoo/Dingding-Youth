package com.youshi.zebra.sms.impl;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import com.dorado.framework.constants.InProduction;
import com.youshi.zebra.core.utils.HttpClientHolder;
import com.youshi.zebra.sms.SmsChannel;
import com.youshi.zebra.sms.SmsException;
import com.youshi.zebra.sms.SmsResult;

/**
 * 百分信息 短信接口
 * 
 * @author wangsch
 * 
 * @date 2016-09-12
 */
@Service("lmoblileSmsChannel")
public class LmoblileSmsChannel implements SmsChannel {

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    /** 用户id */
    private static final String S_NAME = "dldllbys";

    /** 密码 */
    private static final String S_PWD = "ddlad2017";

    /** 企业代码，扩展号，可以空着 */
    private static final String S_CORP_ID = "";

    /** 产品渠道号，这个是触发的 */
    private static final String S_PRD_ID = "1012818";

    private static final String SUBMIT_URL = "http://cf.lmobile.cn/submitdata/service.asmx/g_Submit";

    /** 查剩余短信数 */
    private static final String GET_REMAIN_URL = "http://cf.lmobile.cn/submitdata/service.asmx/Sm_GetRemain";

    public static final int INNNER_EXCEPTION_STATE = -1;

    //@Autowired
    // LmobileMsgIdRecordDAO lmobileMsgIdRecordDAO;


    /**
     */
    @Override
    public SmsResult sendSms(String mobileNumber, String msg) throws SmsException {
	if (StringUtils.isBlank(mobileNumber) || StringUtils.isBlank(msg)) {
	    throwSmsException("mobile num or msg is blank!");
	}

	HttpPost httpPost = new HttpPost(SUBMIT_URL);
	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	nameValuePairs.add(new BasicNameValuePair("sname", S_NAME));
	nameValuePairs.add(new BasicNameValuePair("spwd", S_PWD));
	nameValuePairs.add(new BasicNameValuePair("scorpid", S_CORP_ID));
	nameValuePairs.add(new BasicNameValuePair("sprdid", S_PRD_ID));
	nameValuePairs.add(new BasicNameValuePair("sdst", mobileNumber));
	nameValuePairs.add(new BasicNameValuePair("smsg", msg));
	UrlEncodedFormEntity entity;
	try {
	    entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
	    httpPost.setEntity(entity);
	    HttpResponse response = HttpClientHolder.getHttpClient().execute(httpPost);
	    String content = EntityUtils.toString(response.getEntity(), Charset.defaultCharset()
		    .displayName());
	    SubmitState submitState = new SubmitState(content);
	    //提交成功
	    if (!InProduction.get()) {
		logger.info("send sms {} to mobile {}, result:{}", msg, mobileNumber,
			ReflectionToStringBuilder.toString(submitState));
	    }
	    if (submitState.state == 0) {
		//lmobileMsgIdRecordDAO.record(NumberUtils.toLong(submitState.msgId), mobileNumber, msg);
		return new SmsResult(submitState.msgId);
	    }
	    throwSmsException(submitState.state, submitState.msgState);
	} catch (UnsupportedEncodingException e) {
	    throwSmsException(e);
	} catch (ClientProtocolException e) {
	    throwSmsException(e);
	} catch (IOException e) {
	    throwSmsException(e);
	} catch (DocumentException e) {
	    throwSmsException(e);
	} catch (Throwable e) {
	    throwSmsException(e);
	} finally {
	    httpPost.abort();
	}
	return null;
    }

    @Override
    public int getRemainSms() {
	HttpPost httpPost = new HttpPost(GET_REMAIN_URL);
	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	nameValuePairs.add(new BasicNameValuePair("sname", S_NAME));
	nameValuePairs.add(new BasicNameValuePair("spwd", S_PWD));
	nameValuePairs.add(new BasicNameValuePair("scorpid", S_CORP_ID));
	nameValuePairs.add(new BasicNameValuePair("sprdid", S_PRD_ID));
	UrlEncodedFormEntity entity;
	try {
	    entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
	    httpPost.setEntity(entity);
	    HttpResponse response = HttpClientHolder.getHttpClient().execute(httpPost);
	    String content = EntityUtils.toString(response.getEntity(), Charset.defaultCharset()
		    .displayName());
	    GetRemainState getRemainState = new GetRemainState(content);
	    if (getRemainState.state == 0) {
		return getRemainState.remain;
	    } else {
		return -1;
	    }
	} catch (Throwable e) {
	    logger.error("get remain err!", e);
	} finally {
	    httpPost.abort();
	}
	return -1;
    }

    private void throwSmsException(int state, String msgState) throws MobileSmsException {
	throw new MobileSmsException(state, msgState);
    }

    private void throwSmsException(Throwable e) throws MobileSmsException {
	throw new MobileSmsException(INNNER_EXCEPTION_STATE, e);
    }

    private void throwSmsException(String msgState) throws MobileSmsException {
	throwSmsException(INNNER_EXCEPTION_STATE, msgState);
    }

    static class SubmitState {

	int state;

	String msgId;

	String msgState;

	int reserve;

	public SubmitState(String responseXml) throws DocumentException {
	    SAXReader reader = new SAXReader();
	    StringReader stringReader = new StringReader(responseXml);
	    Document document = reader.read(stringReader);
	    Element rootElement = document.getRootElement();
	    state = Integer.parseInt(rootElement.elementText("State"));
	    msgId = rootElement.elementText("MsgID");
	    msgState = rootElement.elementText("MsgState");
	    reserve = Integer.parseInt(rootElement.elementText("Reserve"));
	    stringReader.close();
	}
    }

    static class GetRemainState {

	int state;

	int remain;

	public GetRemainState(String responseXml) throws DocumentException {
	    SAXReader reader = new SAXReader();
	    StringReader stringReader = new StringReader(responseXml);
	    Document document = reader.read(stringReader);
	    Element rootElement = document.getRootElement();
	    state = Integer.parseInt(rootElement.elementText("State"));
	    remain = Integer.parseInt(rootElement.elementText("Remain"));
	    stringReader.close();
	}
    }

    public static void main(String[] args) throws SmsException, DocumentException {
		LmoblileSmsChannel lmoblileSmsChannel = new LmoblileSmsChannel();
		lmoblileSmsChannel.sendSms("15712889750", "您的验证码是1234【优蛋】");
	//	System.out.println("remain:" + lmoblileSmsChannel.getRemainSms());
    }
}
