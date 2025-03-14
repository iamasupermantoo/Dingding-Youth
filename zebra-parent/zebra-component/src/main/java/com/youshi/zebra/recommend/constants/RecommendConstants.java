package com.youshi.zebra.recommend.constants;

import com.dorado.framework.constants.InProduction;

public class RecommendConstants {
	public static byte[] BANNER_DES_KEY = InProduction.get() ? new byte[] { 90, 0, -7, 28,
            -12, 19, -15, 22 } : new byte[] { 18, 39, 90, -38, -11, -11, 3, -69 };
	
	/*public static byte[] FEED_DES_KEY = InProduction.get() ? new byte[] { 90, 22, -7, 28,
            -12, 19, -15, 5 } : new byte[] { 18, 39, 90, 98, -11, -11, 3, -11 };*/
	public static byte[] FEED_DES_KEY = InProduction.get() ? new byte[] { 99, 22, 38, 8,
            33, -57, 9, 72 } : new byte[] { 15, -40, 44, 8, -12, -45, -101, -60 };
	
	public static String REDISKET = "recommendFeed";
}
