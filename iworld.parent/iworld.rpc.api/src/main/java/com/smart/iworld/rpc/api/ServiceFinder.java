package com.smart.iworld.rpc.api;

import com.smart.iworld.rpc.api.exception.ServiceNotFoundException;

public interface ServiceFinder {

	public ServiceInfo getServiceInfo(String serviceName) throws ServiceNotFoundException;
}
