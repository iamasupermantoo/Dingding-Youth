package com.youshi.zebra.live.constants;

/**
 * 课堂直播，房间的实时信息。信息存在redis hash中，这个类中的常量字段，代表redis hash的字段名。
 * 
 * @author wangsch
 * @date 2017年3月17日
 */
public class RoomInfoField {
	/** 本堂课，frames信息（课件信息），json字符串 */
	public static final String FRAMES_INFO_JSON = "fi";
	
	/** 当前课件x坐标 */
	public static final String CURRENT_FRAME_X = "fx";
	
	/** 当前课件y坐标 */
	public static final String CURRENT_FRAME_Y = "fy";
	
	/** 
	 * 老师在线状态 {@link TeacherOnlineStatus} 
	 */
	public static final String TEACHER_ONLINE_STATUS = "tos";

	/** 
	 * 学生在线状态 {@link StudentOnlineStatus} 
	 */
	public static final String STUDENT_ONLINE_STATUS = "sos";
	
//	/**
//	 * 家长在线状态
//	 */
//	public static final String PARENT_ONLINE_STATUS = "pos";
	
	/** 观众列表，旁观这堂直播课的用户id集合 */
	public static final String AUDIENCE_LIST = "al";
		
}