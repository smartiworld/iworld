package com.smart.iworld.rpc.discover;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smart.iworld.rpc.api.AbstractServiceFinder;
import com.smart.iworld.rpc.api.ServiceInfo;
import com.smart.iworld.rpc.api.exception.ServiceNotFoundException;

public class DefautServiceFinder extends AbstractServiceFinder {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefautServiceFinder.class);
	
	@Override
	public ServiceInfo find(List<ServiceInfo> serviceInfos) throws ServiceNotFoundException {
		try {
			return serviceInfos.get(0);
		} catch (Exception e) {
			LOGGER.error("find servie error:{}", e);
			throw new ServiceNotFoundException(e);
		}
	}

}
