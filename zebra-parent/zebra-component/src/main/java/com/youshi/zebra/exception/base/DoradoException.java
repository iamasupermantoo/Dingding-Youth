package com.youshi.zebra.exception.base;

/**
 * 检查型异常，需要上层调用者处理异常时使用。
* 
* Date: May 17, 2016
* 
 * @author wangsch
 * 
 * @see DoradoMetaCodeException
 */
@SuppressWarnings("serial")
public class DoradoException extends Exception {

    protected DoradoException() {
        super();
    }
    
    protected DoradoException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    protected DoradoException(String message, Throwable cause) {
        super(message, cause);
    }

    protected DoradoException(String message) {
        super(message);
    }

    protected DoradoException(Throwable cause) {
        super(cause);
    }

}
