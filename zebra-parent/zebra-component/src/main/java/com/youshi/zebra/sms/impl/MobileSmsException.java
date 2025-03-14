package com.youshi.zebra.sms.impl;

import com.youshi.zebra.sms.SmsException;
/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public class MobileSmsException extends SmsException {

    private static final long serialVersionUID = 7416735625425584674L;

    private int state;

    private String msgState;

    public MobileSmsException(int state, String msgState) {
        super("state:" + state + ",msg:" + msgState);
        this.state = state;
        this.msgState = msgState;
    }

    public MobileSmsException(int state, Throwable cause) {
        super(cause);
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public String getMsgState() {
        return msgState;
    }

    @Override
    public String toString() {
        return "ImobileSmsException [state=" + state + ", msgState=" + msgState + "]";
    }
}
