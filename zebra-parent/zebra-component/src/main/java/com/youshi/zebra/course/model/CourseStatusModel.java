package com.youshi.zebra.course.model;

import com.dorado.framework.crud.model.impl.AbstractModel;

/**
 * 课程当前的状态信息
 * 
 * @author wangsch
 * @date 2017年3月27日
 */
public class CourseStatusModel extends AbstractModel<Integer>{
	public enum CourseKeys {
		/** id来自{@link CourseModel#getId()} */
		id,
		
		// data字段
		/** 已排lesson，cnt最大值 */
		planed_max_cnt,
		
		/** 已完成的lesson，cnt最大值 */
		finished_max_cnt,
		
	}
	
	private int planedMaxCnt;
	
	private int finishedMaxCnt;
	
	public CourseStatusModel(int id, int planedMaxCnt, int finishedMaxCnt, long createTime) {
		this.id = id;
		this.createTime = createTime;
		this.planedMaxCnt = planedMaxCnt;
		this.finishedMaxCnt = finishedMaxCnt;
	}

	public int getPlanedMaxCnt() {
		return planedMaxCnt;
	}
	
	public int getFinishedMaxCnt() {
		return finishedMaxCnt;
	}
	
}
