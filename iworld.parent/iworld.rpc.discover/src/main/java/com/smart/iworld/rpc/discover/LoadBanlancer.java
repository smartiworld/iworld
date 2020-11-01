package com.smart.iworld.rpc.discover;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smart.iworld.rpc.api.ServiceFinder;
import com.smart.iworld.rpc.api.ServiceInfo;
import com.smart.iworld.rpc.api.exception.RcpException;
import com.smart.iworld.rpc.api.exception.ServiceNotFoundException;
/***
 * 本地缓存服务提供者注册消息
 * 
 * @author caigq
 *
 */
public class LoadBanlancer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoadBanlancer.class);

	private volatile static ConcurrentMap<String, List<ServiceInfo>> services = new ConcurrentHashMap<>();
	
	private volatile static List<ServiceInfo> allServiceInfo = Collections.synchronizedList(new ArrayList<ServiceInfo>());
	
	private static ReentrantReadWriteLock allServiceInfoLock = new ReentrantReadWriteLock();
	
	public List<ServiceInfo> getServiceInfos(String superService) throws RcpException {
		List<ServiceInfo> serviceInfos = services.get(superService);
		if(serviceInfos == null || serviceInfos.size()<0) {
			if(serviceInfos == null || serviceInfos.size()<0) {
				throw new RcpException("service is not find from cache");
			}
		}
		return serviceInfos;
	}
	
	public static void main(String[] args) {
		String s ="aaa|bbb|ccc|";
		String[] split = s.split("\\|", 0);
		System.out.println(split.length);
		for(String ss : split) {
			System.out.println(ss);
		}
	}
	
	public ServiceInfo getServiceInfo(String superService, ServiceFinder finder) throws RcpException {
		List<ServiceInfo> serviceInfos = services.get(superService);
		ServiceInfo serviceInfo = null;
		try {
			serviceInfo = finder.find(serviceInfos);
			if(serviceInfo == null) {
				throw new ServiceNotFoundException("service is not find from cache");
			}
		} catch (ServiceNotFoundException e) {
			LOGGER.error("service is not find from cache:{}", e);
			throw new RcpException("service is not find from cache");
		}
		return serviceInfo;
	}
	
	public void setServiceInfo(String superService, List<ServiceInfo> serviceInfos) {
		services.put(superService, serviceInfos);
	}
	
	public void setServiceInfo(String superService, ServiceInfo serviceInfo) {
		WriteLock allServiceWriteLock = allServiceInfoLock.writeLock();
		try {
			allServiceWriteLock.lock();
			List<ServiceInfo> serviceInfos = services.get(superService);
			if(serviceInfos == null) {
				serviceInfos = new ArrayList<>();
				services.put(superService, serviceInfos);
			}
			serviceInfos.add(serviceInfo);
		} finally{
			allServiceWriteLock.unlock();
		}
	}
	
	public void deleteServiceInfo(String superService) {
		services.remove(superService);
	}
}
