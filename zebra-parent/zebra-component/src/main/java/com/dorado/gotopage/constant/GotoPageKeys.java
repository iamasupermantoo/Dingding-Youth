package com.dorado.gotopage.constant;

/**
 * gotoPage参数
 * 
 * @author wangsch
 * @date		2016年11月8日
 */
public enum GotoPageKeys {
	dataId("dataId"),
	
	// url
	url("url"),
	
	// profile
	userId("dataId"),
	;
	
	public static final int EMPTY_DATA_ID = 0;
	
	/**
	 * 真正存在的key
	 */
	private String key;
	
	private GotoPageKeys(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
	@Override
	public String toString() {
		return key;
	}
	
}
