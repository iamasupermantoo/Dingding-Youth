package com.youshi.zebra.lesson.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

/**
 * 一节课的状态
 * 
 * @author wangsch
 * @date 2017年2月14日
 */
public enum LessonStatus {
	
	/**
	 * 待上课，排课成功后为此状态，这时已经有了安排了上课时间、老师等信息
	 */
	WAIT(0, "待上课"),
	ONPROCESS(1, "正在上课"),
	FINISHED(2, "完成上课"),
	FAIL(3, "失败（具体原因见备注）"),
	
	Added(4, "刚刚添加，还未安排时间")
	
	;
	
	private final int value;
	private final String name;
	LessonStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName() {
    	return name;
    }

    private static final IntObjectMap<LessonStatus> map = new IntObjectOpenHashMap<>();
    static {
        for (LessonStatus e : LessonStatus.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final LessonStatus fromValue(Integer value) {
        return map.get(value);
    }
}
