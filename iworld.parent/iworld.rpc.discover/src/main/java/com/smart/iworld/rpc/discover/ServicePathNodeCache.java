package com.smart.iworld.rpc.discover;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.smart.iworld.rpc.api.ServiceInfo;
import com.smart.iworld.rpc.api.exception.RcpException;
/***
 * 本地缓存服务提供者注册消息
 * 
 * @author admin
 *
 */
public class ServicePathNodeCache {

	private static ConcurrentMap<String, List<ServiceInfo>> services = new ConcurrentHashMap<>();
	
	
	public ServicePathNodeCache() {
	}
	
	public List<ServiceInfo> getServiceInfo(String superService) throws RcpException {
		List<ServiceInfo> serviceInfos = services.get(superService);
		if(serviceInfos == null || serviceInfos.size()<0) {
			if(serviceInfos == null || serviceInfos.size()<0) {
				throw new RcpException("service is not find from cache");
			}
		}
		return serviceInfos;
	}
	
	public void setServiceInfo(String superService, List<ServiceInfo> serviceInfos) {
		services.put(superService, serviceInfos);
	}
}
