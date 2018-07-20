package com.smart.iworld.rpc.discover;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.smart.iworld.rpc.api.ServiceFinder;
import com.smart.iworld.rpc.api.ServiceInfo;
import com.smart.iworld.rpc.api.exception.RcpException;
/***
 * 本地缓存服务提供者注册消息
 * 
 * @author caigq
 *
 */
public class ServicePathNodeCache {

	private static ConcurrentMap<String, List<ServiceInfo>> services = new ConcurrentHashMap<>();
	
	public static List<ServiceInfo> getServiceInfo(String superService) throws RcpException {
		List<ServiceInfo> serviceInfos = services.get(superService);
		if(serviceInfos == null || serviceInfos.size()<0) {
			if(serviceInfos == null || serviceInfos.size()<0) {
				throw new RcpException("service is not find from cache");
			}
		}
		return serviceInfos;
	}
	
	public static ServiceInfo getServiceInfo(String superService, ServiceFinder finder) throws RcpException {
		List<ServiceInfo> serviceInfos = services.get(superService);
		if(serviceInfos == null || serviceInfos.size()<0) {
			if(serviceInfos == null || serviceInfos.size()<0) {
				throw new RcpException("service is not find from cache");
			}
		}
		return null;
	}
	
	public static void setServiceInfo(String superService, List<ServiceInfo> serviceInfos) {
		services.put(superService, serviceInfos);
	}
	
	public static void setServiceInfo(String superService, ServiceInfo serviceInfo) {
		List<ServiceInfo> serviceInfos = services.get(superService);
		if(serviceInfos == null) {
			serviceInfos = new ArrayList<>();
			services.put(superService, serviceInfos);
		}
		serviceInfos.add(serviceInfo);
	}
	
	public static void deleteServiceInfo(String superService) {
		services.remove(superService);
	}
}
