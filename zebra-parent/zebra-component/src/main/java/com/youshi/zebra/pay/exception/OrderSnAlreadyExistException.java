package com.youshi.zebra.pay.exception;

import com.youshi.zebra.exception.base.DoradoException;

/**
 * 
 * @author wangsch
 * @date 2017年4月28日
 */
@SuppressWarnings("serial")
public class OrderSnAlreadyExistException extends DoradoException {
	
	public OrderSnAlreadyExistException(String message) {
		super(message);
	}
	
}
