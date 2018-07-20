package com.smart.iworld.rpc.api;

import com.smart.iworld.rpc.api.exception.ServiceNotFoundException;

public interface ServiceFinder {

	/**
	 * 通过调用接口查询要调用的目标
	 * @param serviceName
	 * @return
	 * @throws ServiceNotFoundException
	 */
	public ServiceInfo getServiceInfo(String serviceName) throws ServiceNotFoundException;
	
}
