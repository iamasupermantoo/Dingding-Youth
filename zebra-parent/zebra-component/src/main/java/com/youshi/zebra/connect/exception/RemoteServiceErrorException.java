/**
 * 
 */
package com.youshi.zebra.connect.exception;

import com.youshi.zebra.exception.base.DoradoException;

/**
 * 调用第三方账号服务失败
 * 
 * @author wangsch
 * @date 2017年1月10日
 */
public class RemoteServiceErrorException extends DoradoException {

    private static final long serialVersionUID = 4105770170635864533L;

    public RemoteServiceErrorException() {
        super();
    }

    public RemoteServiceErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteServiceErrorException(String message) {
        super(message);
    }

    public RemoteServiceErrorException(Throwable cause) {
        super(cause);
    }

}
