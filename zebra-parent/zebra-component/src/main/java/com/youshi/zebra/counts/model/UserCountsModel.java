package com.youshi.zebra.counts.model;

import java.util.Map;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 用户的一些计数
 * 
 * @author wangsch
 * @date		2016年11月6日
 *
 */
public class UserCountsModel extends AbstractModel<Integer>{
	private int courseCount;
	
	private int finishedCount;
	
	private int duration;
	
	public UserCountsModel(int id, int courseCount, int finishedCount, int duration) {
		this.id = id;					// 请注意，id代表“用户id”，和user表是一一对应
		this.courseCount = courseCount;
		this.finishedCount = finishedCount;
		this.duration = duration;
	}
	
	public int getCourseCount() {
		return courseCount;
	}

	public void setCourseCount(int courseCount) {
		this.courseCount = courseCount;
	}

	public int getFinishedCount() {
		return finishedCount;
	}

	public void setFinishedCount(int finishedCount) {
		this.finishedCount = finishedCount;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	// -----------------------不支持的操作-----------------------
	/* (non-Javadoc)
	 * @see com.dorado.framework.model.HasUuid#getUuid()
	 */
	@Override
	@JsonIgnore
	public String getUuid() {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see com.dorado.framework.model.AbstractModel#resolvedData()
	 */
	@Override
	public Map<String, Object> resolvedData() {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see com.dorado.framework.model.AbstractModel#getStatus()
	 */
	@Override
	@JsonIgnore
	public int getStatus() {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see com.dorado.framework.model.AbstractModel#setStatus(int)
	 */
	@Override
	public void setStatus(int status) {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see com.dorado.framework.model.AbstractModel#getData()
	 */
	@Override
	@JsonIgnore
	public String getData() {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see com.dorado.framework.model.AbstractModel#setData(java.lang.String)
	 */
	@Override
	public void setData(String data) {
		throw new UnsupportedOperationException();
	}
	// -----------------------end-----------------------
}
