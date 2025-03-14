/**
 * 
 */
package com.youshi.zebra.connect.exception;
import com.youshi.zebra.exception.base.DoradoException;

/**
 * 系统内用户不存在
 * 
 * @author wangsch
 * @date 2017年1月10日
 */
public class UserNotExistException extends DoradoException {

    private static final long serialVersionUID = 5829852022143356311L;

    public UserNotExistException(String message) {
        super(message);
    }

}
