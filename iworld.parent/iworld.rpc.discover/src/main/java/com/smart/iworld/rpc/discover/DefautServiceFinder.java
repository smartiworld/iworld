package com.smart.iworld.rpc.discover;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smart.iworld.rpc.api.ServiceFinder;
import com.smart.iworld.rpc.api.ServiceInfo;
import com.smart.iworld.rpc.api.exception.RcpException;
import com.smart.iworld.rpc.api.exception.ServiceNotFoundException;

public class DefautServiceFinder implements ServiceFinder {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefautServiceFinder.class);
	
	private IServiceDiscover serviceDiscover;
	
	public DefautServiceFinder(IServiceDiscover serviceDiscover) {
		this.serviceDiscover = serviceDiscover;
	}
	
	@Override
	public ServiceInfo getServiceInfo(String serviceName) throws ServiceNotFoundException {
		try {
			List<ServiceInfo> services = serviceDiscover.discoverService(serviceName);
			return services.get(0);
		} catch (RcpException e) {
			LOGGER.error("find servie error:{}", e);
			throw new ServiceNotFoundException(e);
		}
	}

}
