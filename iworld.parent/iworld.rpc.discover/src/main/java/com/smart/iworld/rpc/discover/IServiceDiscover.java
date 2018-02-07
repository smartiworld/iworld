package com.smart.iworld.rpc.discover;

import java.util.List;

import com.smart.iworld.rpc.api.ServiceInfo;
import com.smart.iworld.rpc.api.exception.RcpException;

public interface IServiceDiscover {

	/**
	 * 发现服务
	 * @param serviceName 调用服务
	 * @return 返回服务提供者
	 */
	public List<ServiceInfo> discoverService(String serviceName) throws RcpException;
	
	/**
	 * 从注册中心发现所有服务
	 */
	public void disCoverAllService() throws RcpException ;
}
