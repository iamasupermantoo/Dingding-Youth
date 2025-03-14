package com.youshi.zebra.live.agora;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.common.base.Joiner;

/**
 * 
 * @author wangsch
 * @date 2017年3月14日
 */
public class SignalingKey {
	private static final String SIGNALING_KEY_VERSION = "1";

	public static String generateSignalingKey(String appId, String appCertificate, 
			long account, long expiredTime) {
		
		String md5Hex = DigestUtils.md5Hex(Joiner.on("").join(account, appId, appCertificate, expiredTime));
		String result = Joiner.on(":").join(SIGNALING_KEY_VERSION, appId, expiredTime, md5Hex);
		
		return result;
	}
}
