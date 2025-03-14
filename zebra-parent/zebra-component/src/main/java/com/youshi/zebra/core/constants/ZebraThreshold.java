package com.youshi.zebra.core.constants;

import java.util.concurrent.TimeUnit;

import com.dorado.framework.threshold.Threshold;

/**
 * Threshold 检测配置，原则上所有写请求，都需要加threshold限制。
 * 
 * @author wangsch
 * @date 2017年1月5日
 */
public enum ZebraThreshold implements Threshold {
	/**
	 * 每个手机号发验证码频率
	 */
	MOBILE_CODE("mc", TimeUnit.DAYS.toSeconds(1), 10), 
	
	/**
	 * 每个IP发验证码频率，根据ip
	 */
	MOBILE_CODE_IP("mci", TimeUnit.DAYS.toSeconds(1), 300),
	
	/**
	 * 验证验证码的次数
	 */
	MOBILE_CODE_VERIFY("mcv", TimeUnit.DAYS.toSeconds(1), 10),
	
	/**
	 * 验证验证码的次数，根据ip
	 */
	MOBILE_CODE_VERIFY_IP("mcvi", TimeUnit.DAYS.toSeconds(1), 300),
	
	/** 
	 * 运营后台管理员登陆
	 */
	ADMIN_LOGIN("alogin", TimeUnit.DAYS.toSeconds(1), 20),
	/**
	 * 运营后台管理员登陆，根据ip
	 */
	ADMIN_LOGIN_IP("aloginip", TimeUnit.DAYS.toSeconds(1), 100)
	
	;

	private final String prefix;

	private final int expireSeconds;

	private final int limit;

	ZebraThreshold(String prefix, long expireSeconds, int limit) {
		if (!prefix.endsWith(".")) {
			prefix = prefix + ".";
		}
		this.prefix = prefix;
		this.expireSeconds = (int) expireSeconds;
		this.limit = limit;
	}

	public <T> String build(T param) {
		if (param != null) {
			return prefix + param.toString();
		} else {
			return build();
		}
	}

	public String build() {
		return prefix;
	}

	public int getExpireSeconds() {
		return expireSeconds;
	}

	public int getLimit() {
		return limit;
	}

}