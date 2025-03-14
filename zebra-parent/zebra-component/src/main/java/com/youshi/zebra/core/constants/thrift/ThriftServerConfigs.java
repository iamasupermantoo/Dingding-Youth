package com.youshi.zebra.core.constants.thrift;

import com.dorado.framework.thrift.server.ThriftServerConfig;

/**
 * 
 * 枚举，thrift服务配置。每个thrift服务在这里定义一下配置（名字、端口）
 * 
 * @author wangsch
 * @date 2016-09-12
 */
public enum ThriftServerConfigs implements ThriftServerConfig {
	/** 图片上传 */
	ImageUpload(10001),
	;
	
	private int port;
	
	ThriftServerConfigs(int port) {
		this.port = port;
	}
	
	public String getName() {
		return this.name();
	}
	
	public int getPort() {
		return port;
	}
}
