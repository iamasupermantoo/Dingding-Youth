package com.youshi.zebra.sms;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public interface SmsChannel {

    /**
     * 发送下行短信
     * 
     * @param mobileNumber
     * @param msg
     * @return
     */
    SmsResult sendSms(String mobileNumber, String msg) throws SmsException;

    /**
     * 返回剩余的短信数
     * 
     * @return -1(出错了)
     */
    int getRemainSms();
}
