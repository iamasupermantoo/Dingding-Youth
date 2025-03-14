package com.youshi.zebra.misc.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.mvc.reqcontext.Platform;
import com.github.phantomthief.util.ObjectMapperUtils;

/**
 * 客户端版本更新记录
 * 
 * @author wangsch
 * @date		2016年11月14日
 *
 */
public class ClientVersion extends AbstractModel<Integer>{
	private String fromVersion;
	
	private String version;
	
	private boolean force;
	
	private Platform platform;
	
	private String url;
	
	private String description;
	
	public ClientVersion(Integer id, String fromVersion, String version, boolean force, Platform platform, String url, 
			String description, String data, long createTime) {
		this.id = id;
		this.fromVersion = fromVersion;
		this.version = version;
		this.force = force;
		this.platform = platform;
		this.url = url;
		this.description = description;
		this.data = data;
		this.createTime = createTime;
		
	}

	public String getFromVersion() {
		return fromVersion;
	}
	
	public void setFromVersion(String fromVersion) {
		this.fromVersion = fromVersion;
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean getForce() {
		return force;
	}
	
	public void setForce(boolean force) {
		this.force = force;
	}
	
	public Platform getPlatform() {
		return platform;
	}
	
	public void setPlatform(Platform platform) {
		this.platform = platform;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return ObjectMapperUtils.toJSON(this);
	}
	
}
