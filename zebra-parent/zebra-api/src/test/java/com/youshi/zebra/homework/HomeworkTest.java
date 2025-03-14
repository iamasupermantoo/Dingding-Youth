package com.youshi.zebra.homework;

import java.io.File;

import org.junit.Test;

import com.youshi.zebra.audio.utils.AudioConvertUtils;

/**
 * 
 * @author wangsch
 * @date 2017年2月10日
 */
public class HomeworkTest {
	@Test
	public void mp3length() throws Exception {
		long mp3AudioLength = AudioConvertUtils.getMp3AudioLength(new File("/tmp/4Kcebkq4aUg9RzEFBVF2BQ.mp3"));
		System.out.println(mp3AudioLength);
		mp3AudioLength = AudioConvertUtils.getMp3AudioLength(new File("/tmp/Ariana Grande - Intro.mp3"));
		System.out.println(mp3AudioLength);
	}
}
