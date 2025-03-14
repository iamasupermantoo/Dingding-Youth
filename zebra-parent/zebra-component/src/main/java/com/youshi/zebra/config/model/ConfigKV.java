package com.youshi.zebra.config.model;

public class ConfigKV {
	public final String key;
	public final Object value;
	
	public ConfigKV(String key, Object value) {
		this.key = key;
		this.value = value;
	}
}