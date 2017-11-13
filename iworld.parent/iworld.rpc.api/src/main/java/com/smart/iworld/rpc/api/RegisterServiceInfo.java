package com.smart.iworld.rpc.api;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * 注册服务实体类信息
 * 
 * @author iworld
 *
 */
public class RegisterServiceInfo implements Serializable {
	
	private static final long serialVersionUID = 8878356232108607152L;

	private String interfaceName;
	
	private String servicClassName;
	
	private String url;
	
	private Integer priority;

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getServicClassName() {
		return servicClassName;
	}

	public void setServicClassName(String servicClassName) {
		this.servicClassName = servicClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
}
