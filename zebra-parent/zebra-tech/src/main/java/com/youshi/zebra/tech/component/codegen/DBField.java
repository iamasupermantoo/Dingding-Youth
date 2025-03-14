package com.youshi.zebra.tech.component.codegen;

/**
 * 代表一个数据库字段
 * 
 * @author wangsch
 * @date 2017年4月7日
 */
public class DBField {
	/** 数据库字段名 */
	private String name;
	
	/** Java代码，数据类型 */
	private JavaType type;
	
	/** java代码，参数名 */
	private String paramName;
	
	/** java代码，sql中的placeholder */
	private String placeholder;
	
	/** 是否是最后一个字段 */
	private boolean isLast = false;

	public DBField(JavaType type, String name, String paramName, String placeholder) {
		this.type = type;
		this.name = name;
		this.paramName = paramName;
		this.placeholder = placeholder;
	}
	
	public DBField(JavaType type, String name) {
		this.type = type;
		this.name = name;
		this.paramName = FTLUtils.toCamelStyle(name);
		this.placeholder = paramName;
	}
	
	public DBField(JavaType type, String name, boolean isLast) {
		this.type = type;
		this.name = name;
		this.paramName = FTLUtils.toCamelStyle(name);
		this.placeholder = paramName;
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
	
	public String getPlaceholder() {
		return placeholder;
	}

	public boolean getIsLast() {
		return isLast;
	}

	public void setIsLast(boolean isLast) {
		this.isLast = isLast;
	}
}