package com.youshi.zebra.core.utils;

import com.youshi.zebra.exception.common.DAOException;

/**
 * DAO工具
 * 
 * @author wangsch
 * @date 2016-09-22
 */
public class RedisDAOUtils {
	public static final String REDIS_RET_OK = "OK";
	
	public static final Long REDIS_NUM_RET_1 = 1L;
	
	public static void checkOK(String ret) {
		if(!REDIS_RET_OK.equals(ret)) {
			throw new DAOException("Ret NOT OK. ret: " + ret);
		}
	}
	
	public static void checkNumRet(Long ret, Long require) {
		if(REDIS_NUM_RET_1.intValue() != ret) {
			throw new DAOException("Num ret NOT MATCH. ret: " + ret + ", require: " + require);
		}
	}
	
	public static void checkRet_1(Long ret) {
		checkNumRet(ret, REDIS_NUM_RET_1);
	}
	
	
	
	
}
