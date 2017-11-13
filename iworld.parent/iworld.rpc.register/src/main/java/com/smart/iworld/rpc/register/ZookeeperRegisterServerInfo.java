package com.smart.iworld.rpc.register;


import com.smart.iworld.rpc.api.RegisterServerInfo;

/**
 * 注册中心配置
 * 
 * @author iworld
 * 
 */
public class ZookeeperRegisterServerInfo extends RegisterServerInfo {


	private static final long serialVersionUID = 4134930437641933294L;
	/**
	 * zookeeper集群地址格式多个地址中间用","隔开
	 * 例如 192.168.1.1:2181,192.168.1.2:2181,.,.
	 */
	private String connetString;
	/**其他注册中心需要单独提供端口号*/
	private Integer port;
	
	private Integer sessionTimeout;
	/**注册中心父节点*/
	private String parentPath;

	public String getConnetString() {
		return connetString;
	}

	public void setConnetString(String connetString) {
		this.connetString = connetString;
	}
	
	/**
	 * 
	 */
	@Override
	public Integer getPort() {
		return port;
	}

	@Override
	public void setPort(Integer port) {
		this.port = port;
	}

	public Integer getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(Integer sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

}
