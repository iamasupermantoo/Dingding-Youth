package com.youshi.zebra.tech.component.codegen.ftl;

/**
 * 
 * @author wangsch
 * @date 2017年4月11日
 */
public class ServiceFtl {
	private String basePackage;
	
	private String model;
	
	private String service;
	
	private String dao;
	
	public ServiceFtl setBasePackage(String basePackage) {
		this.basePackage = basePackage;
		return this;
	}

	public ServiceFtl setModel(String model) {
		this.model = model;
		return this;
	}
	
	public ServiceFtl setService(String service) {
		this.service = service;
		return this;
	}
	
	public ServiceFtl setDao(String dao) {
		this.dao = dao;
		return this;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public String getModel() {
		return model;
	}

	public String getService() {
		return service;
	}

	public String getDao() {
		return dao;
	}
}
