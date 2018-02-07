package com.smart.iworld.rpc.api;
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
	
	private Class<? extends Object> servicClass;
	
	private String url;
	
	private Integer priority;
	
	private Integer port;

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public Class<? extends Object> getServicClass() {
		return servicClass;
	}

	public void setServicClass(Class<? extends Object> servicClass) {
		this.servicClass = servicClass;
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

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
}
