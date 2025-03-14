package com.youshi.zebra.tech.component.codegen.ftl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.youshi.zebra.tech.component.codegen.DBField;

/**
 * 
 * @author wangsch
 * @date 2017年4月7日
 */
public class DAOFtl {
	private String basePackage;
	
	private String model;
	
	private String table;
	
	private String dao;
	
	private String enumStatus;
	
	private List<DBField> dbFields;
	
	public DAOFtl() {
		this.dbFields = new ArrayList<>();
	}
	
	public DAOFtl setBasePackage(String basePackage) {
		this.basePackage = basePackage;
		return this;
	}

	public DAOFtl setModel(String model) {
		this.model = model;
		return this;
	}
	
	public DAOFtl setTable(String table) {
		this.table = table;
		return this;
	}
	
	public DAOFtl setDao(String dao) {
		this.dao = dao;
		return this;
	}
	
	public DAOFtl setEnumStatus(String enumStatus) {
		this.enumStatus = enumStatus;
		return this;
	}
	
	public DAOFtl addDBFields(Collection<DBField> dbFields) {
		this.dbFields.addAll(dbFields);
		return this;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public String getModel() {
		return model;
	}
	
	public String getTable() {
		return table;
	}
	
	public String getDao() {
		return dao;
	}

	public String getEnumStatus() {
		return enumStatus;
	}
	
	public List<DBField> getDbFields() {
		return dbFields;
	}
}
