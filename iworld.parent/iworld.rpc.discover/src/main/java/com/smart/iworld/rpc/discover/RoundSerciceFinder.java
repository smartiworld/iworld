package com.smart.iworld.rpc.discover;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.smart.iworld.rpc.api.AbstractServiceFinder;
import com.smart.iworld.rpc.api.ServiceInfo;
import com.smart.iworld.rpc.api.exception.ServiceNotFoundException;

public class RoundSerciceFinder extends AbstractServiceFinder {
	
	private AtomicInteger nextServiceIndex = null;
	
	public RoundSerciceFinder() {
		nextServiceIndex = new AtomicInteger(0);
	}

	@Override
	public ServiceInfo find(List<ServiceInfo> serviceInfos) throws ServiceNotFoundException {
		int allServiceSize = 0;
		if(serviceInfos == null || (allServiceSize = serviceInfos.size()) == 0) {
			return null;
		}
		
		ServiceInfo serviceInfo = null;
		while(serviceInfo == null) {
			int next = getNextServiceIndex(allServiceSize);
			serviceInfo = serviceInfos.get(next);
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
	
	private int getNextServiceIndex(int allServiceSize) {
		for (;;) {
			int current = nextServiceIndex.get();
			int next = (current + 1) % allServiceSize;
			if(nextServiceIndex.compareAndSet(current, next)) {
				return next;
			}
		}
	}

}
