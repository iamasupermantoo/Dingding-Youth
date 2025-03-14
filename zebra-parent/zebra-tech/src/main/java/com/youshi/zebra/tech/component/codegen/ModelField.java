package com.youshi.zebra.tech.component.codegen;

/**
 * 代表一个Model的属性
 * 
 * @author wangsch
 * @date 2017年4月7日
 */
@Deprecated
public class ModelField {
	private JavaType type;
	private String name;
	private boolean isLast = false;

	public ModelField(JavaType type, String name) {
		this.type = type;
		this.name = name;
	}

	public String getType() {
		return type.getName();
	}

	public String getName() {
		return name;
	}

	public boolean getIsLast() {
		return isLast;
	}

	public void setIsLast(boolean isLast) {
		this.isLast = isLast;
	}
}