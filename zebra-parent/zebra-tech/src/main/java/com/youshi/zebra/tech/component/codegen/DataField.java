package com.youshi.zebra.tech.component.codegen;

/**
 * 
 * @author wangsch
 * @date 2017年4月7日
 */
public class DataField {
	private JavaType type;
	private String name;
	private String paramName;
	private boolean isLast = false;

	public DataField(JavaType type, String name, String paramName) {
		this.type = type;
		this.name = name;
		this.paramName = paramName;
	}
	
	public DataField(JavaType type, String name) {
		this.type = type;
		this.name = name;
		this.paramName = FTLUtils.toCamelStyle(name);
	}
	
	public DataField(JavaType type, String name, boolean isLast) {
		this.type = type;
		this.name = name;
		this.paramName = FTLUtils.toCamelStyle(name);
		this.isLast = isLast;
	}

	public String getType() {
		return type.getName();
	}

	public String getName() {
		return name;
	}
	
	public String getParamName() {
		return paramName;
	}

	public boolean getIsLast() {
		return isLast;
	}

	public void setIsLast(boolean isLast) {
		this.isLast = isLast;
	}
}