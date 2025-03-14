package com.youshi.zebra.mobile.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

/**
 * 手机注册、找回密码相关状态
 * 
 * @author wangsch
 * @date 2017年1月10日
 */
public enum MobileCodeStatus {
	/** 验证码待验证 */
	MobileCodePending(14),

	/** 验证码验证成功 */
	MobileCodeVerified(15),

	/** 验证码无效（验证失败超过阀值） */
	MobileCodeInvalid(16),
	
	/** 验证码已经过期 */
	MobileCodeExpired(17),

	/** 号已经被注册了 */
	MobileRegistered(18),
	
	;

	private int value;

	private String name;

	MobileCodeStatus(int value) {
		this.value = value;
	}

	MobileCodeStatus(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	private static final IntObjectMap<MobileCodeStatus> map = new IntObjectOpenHashMap<>();
	static {
		for (MobileCodeStatus e : MobileCodeStatus.values()) {
			map.put(e.getValue(), e);
		}
	}

	public static final MobileCodeStatus fromValue(Integer status) {
		return map.get(status);
	}
}
