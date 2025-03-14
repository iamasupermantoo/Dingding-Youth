package com.youshi.zebra.sms;

public class SmsException extends Exception {

    private static final long serialVersionUID = -5440810977105003271L;

    public SmsException() {
        super();
    }

    public SmsException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SmsException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmsException(String message) {
        super(message);
    }

    public SmsException(Throwable cause) {
        super(cause);
    }

}
