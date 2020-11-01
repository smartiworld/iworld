package com.smart.iworld.rpc.discover;

import java.util.List;
import java.util.Random;

import com.smart.iworld.rpc.api.AbstractServiceFinder;
import com.smart.iworld.rpc.api.ServiceInfo;
import com.smart.iworld.rpc.api.exception.ServiceNotFoundException;

public class RandomServiceFinder extends AbstractServiceFinder {

	private Random random = null;
	
	public RandomServiceFinder() {
		random = new Random();
	}
	
	@Override
	public ServiceInfo find(List<ServiceInfo> serviceInfos) throws ServiceNotFoundException {
		if(serviceInfos == null || serviceInfos.size() < 0) {
			return null;
		}
		ServiceInfo serviceInfo = null;
		while(serviceInfo == null) {
			int allServiceSize = serviceInfos.size();
			if(allServiceSize == 0) {
				return null;
			}
			int serviceIndex = random.nextInt(allServiceSize);
			serviceInfo = serviceInfos.get(serviceIndex);
			if(serviceInfo == null) {
				Thread.yield();
                continue;
			}
			if(serviceInfo.isAlive()) {
				return serviceInfo;
			}
			serviceInfo = null;
			Thread.yield();
		}
		return serviceInfo;
	}

}
