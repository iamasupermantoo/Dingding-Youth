package com.youshi.zebra.tech.component.codegen.ftl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.youshi.zebra.tech.component.codegen.DBField;
import com.youshi.zebra.tech.component.codegen.DataField;

/**
 * 
 * @author wangsch
 * @date 2017年4月7日
 */
public class ModelFtl {
	private String basePackage;
	
	private String model;
	
	private String keysEnum;
	
	private List<DBField> dbFields;
	
	private List<DataField> dataFields;
	
	public ModelFtl() {
		this.dbFields = new ArrayList<>();
		this.dataFields = new ArrayList<>();
	}
	
	public ModelFtl setBasePackage(String basePackage) {
		this.basePackage = basePackage;
		return this;
	}

	public ModelFtl setModel(String model) {
		this.model = model;
		return this;
	}
	
	public ModelFtl setKeysEnum(String keysEnum) {
		this.keysEnum = keysEnum;
		return this;
	}
	
	public ModelFtl addDBFields(Collection<DBField> dbFields) {
		this.dbFields.addAll(dbFields);
		return this;
	}

	public ModelFtl addDataField(DataField dataField) {
		this.dataFields.add(dataField);
		return this;
	}
	
	public ModelFtl addDataFields(Collection<DataField> dataFields) {
		this.dataFields.addAll(dataFields);
		return this;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public String getModel() {
		return model;
	}

	public String getKeysEnum() {
		return keysEnum;
	}
	
	public List<DBField> getDbFields() {
		return dbFields;
	}
	
	public List<DataField> getDataFields() {
		return dataFields;
	}
}
