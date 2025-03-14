package com.youshi.zebra.sms;

public class SmsResult {

    private String msgId;

    public SmsResult(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgId() {
        return msgId;
    }
}
