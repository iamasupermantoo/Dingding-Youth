package com.youshi.zebra.tech.component.codegen;

public enum JavaType {
	INT("int"), 
	LONG("long"), 
	STRING("String"), 
	LIST("java.util.List"),
	BOOLEAN("boolean"),
	;
	
	private String name;

	JavaType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}