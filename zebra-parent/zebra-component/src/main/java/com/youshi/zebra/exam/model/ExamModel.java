package com.youshi.zebra.exam.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;

/**
 * 
 * @author codegen
 */
public class ExamModel extends AbstractModel<Integer>{
	public enum ExamKeys {
		// db 字段
		level,
		name,
		status,
	
		// data字段
	}
	
	private Integer level;
	private String name;
	
	public ExamModel(
			int id, String data, long createTime, int status,
			Integer level,
			String name
			) {
		super(id, data, createTime, status);
		this.level = level;
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}
	public String getName() {
		return name;
	}
	
}
