package com.smart.iworld.rpc.api;

import java.io.Serializable;

/**
 * 注册中心配置
 * 
 * @author iworld
 * 
 */
public class RegisterServerInfo implements Serializable {

	private static final long serialVersionUID = -350785889386458986L;

	/**
	 * zookeeper集群地址格式多个地址中间用","隔开
	 * 例如 192.168.1.1:2181,192.168.1.2:2181,...
	 */
	private String address;
	
	private Integer port;
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

}
