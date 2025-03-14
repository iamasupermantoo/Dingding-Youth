package com.youshi.zebra.live.utils;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youshi.zebra.exception.base.DoradoRuntimeException;
import com.youshi.zebra.live.agora.SignalingKey;
import com.youshi.zebra.live.constants.AgoraConstants;

import io.agora.media.DynamicKey5;

/**
 * 
 * @author wangsch
 * @date 2017年3月14日
 */
public class AgoraUtils {
	private static final Logger logger = LoggerFactory.getLogger(AgoraUtils.class);
	
	public static final String genChannelKey(String channel, long account) {
		try {
			long currentTime = System.currentTimeMillis();
			long expiredTime = currentTime + TimeUnit.DAYS.toMillis(3);
			
			int unixTs = (int)(currentTime / 1000);
			int l = (int)(expiredTime/1000);
			int rand = RandomUtils.nextInt(0, Integer.MAX_VALUE);
			
//			String key = DynamicKey4.generateMediaChannelKey(AgoraConstants.APP_ID, AgoraConstants.APP_CERTIFICATE, 
//					channel, unixTs, 
//					rand, account, l);
			
			String key = DynamicKey5.generateMediaChannelKey(AgoraConstants.APP_ID, AgoraConstants.APP_CERTIFICATE, 
					channel, unixTs, rand, account, l);
			
			logger.info("curr: " + unixTs + ", extime: " + l + ", account: " + account + ", " + channel + ", rand: " + rand);
			
			return key;
		} catch (Exception e) {
			throw new DoradoRuntimeException(e);
		}
	}
	
	public static final String genSignalingKey(long account) {
		long expiredTime = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(3);
		
		return SignalingKey.generateSignalingKey(AgoraConstants.APP_ID, AgoraConstants.APP_CERTIFICATE, 
				account, expiredTime / 1000);
	}
}
