package com.smart.iworld.rpc.discover;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.smart.iworld.rpc.api.RegisterServiceInfo;
import com.smart.iworld.rpc.api.ServiceInfo;
import com.smart.iworld.rpc.api.ZookeeperServerInfo;
import com.smart.iworld.rpc.api.exception.RcpException;

public class ZookeeperServiceDiscover implements IServiceDiscover {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperServiceDiscover.class);
	
	private ZookeeperServerInfo serverInfo;
	
	private CuratorFramework curator;
	
	private PathChildrenCache parentCache;
	
	private ServicePathNodeCache cache;
	
	public ZookeeperServiceDiscover(ZookeeperServerInfo serverInfo) {
		this.serverInfo = serverInfo;
	}
	
	public void start() throws RcpException {
		curator = CuratorFrameworkFactory.builder()
				.connectString(serverInfo.getConnetString())
				.retryPolicy(new ExponentialBackoffRetry(2000, 3))
				.namespace(serverInfo.getParentPath())
				.build();
		curator.start();
		if(serverInfo.isCache()) {
			cache = new ServicePathNodeCache();
		}
		parentCache = new PathChildrenCache(curator, serverInfo.getParentPath(), true);
		try {
			parentCache.start();
		} catch (Exception e) {
			LOGGER.error("父节点创建缓存失败:{}", e);
			throw new RcpException(e);
		}
		parentCache.getListenable().addListener(new ZookeeperPathEventListener());
	}
	
	@SuppressWarnings("resource")
	@Override
	public List<ServiceInfo> discoverService(String serviceName) throws RcpException {
		if(cache != null) {
			List<ServiceInfo> serviceInfos = cache.getServiceInfo(serviceName);
			return serviceInfos;
		}
		List<ServiceInfo> serviceInfos = new ArrayList<>();
		String parentPath = serverInfo.getParentPath();
		String servicePath = parentPath + "/" + serviceName;
		PathChildrenCache childrenCache = new PathChildrenCache(curator, servicePath, true);
		try {
			childrenCache.start();
			childrenCache.getListenable().addListener(new ZookeeperPathEventListener());
			Stat parentStat = curator.checkExists().forPath(parentPath);
			if(parentStat == null) {
				LOGGER.info("create parent path:{}", parentPath);
				curator.create().forPath(parentPath);
			}
			Stat childrenStat = curator.checkExists().forPath(servicePath);
			if(childrenStat == null) {
				LOGGER.info("create service path:{}", servicePath);
				curator.create().forPath(servicePath);
			}
			List<String> childrens = curator.getChildren().forPath(servicePath);
			for(String children : childrens) {
				byte[] forPath = curator.getData().forPath(children);
				String result = new String(forPath, serverInfo.getCharSet());
				ServiceInfo serviceInfo = JSON.parseObject(result, ServiceInfo.class);
				serviceInfos.add(serviceInfo);
			}
		} catch (Exception e) {
			LOGGER.error("discover service info error:{}", e);
			throw new RcpException(e);
		}
		return serviceInfos;
	}
	
	@Override
	public void disCoverAllService() throws RcpException {
		try {
			String parentPath = serverInfo.getParentPath();
			Stat parentStat = curator.checkExists().forPath(parentPath);
			if(parentStat == null) {
				LOGGER.info("create parent path:{}", parentPath);
				curator.create().forPath(parentPath);
			}
			List<String> childrenNodes = curator.getChildren().forPath(parentPath);
			for(String childrenNode : childrenNodes) {
				List<ServiceInfo> serviceInfos = new ArrayList<>();
				List<String> serviceNodes = curator.getChildren().forPath(childrenNode);
				for(String serviceNode : serviceNodes) {
					byte[] serviceData = curator.getData().forPath(serviceNode);
					String result = new String(serviceData, serverInfo.getCharSet());
					ServiceInfo serviceInfo = JSON.parseObject(result, ServiceInfo.class);
					serviceInfos.add(serviceInfo);
				}
				if(cache == null) {
					cache = new ServicePathNodeCache();
				}
				cache.setServiceInfo(childrenNode, serviceInfos);
			}
		} catch (Exception e) {
			LOGGER.error("discover services info error:{}", e);
			throw new RcpException(e);
		}
		
	}
	
	public void stop() {
		curator.close();
	}
	
	public static void main(String[] args) {
		RegisterServiceInfo rs = new RegisterServiceInfo();
		rs.setInterfaceName(rs.getClass().getName());
		rs.setPriority(1);
		String json = JSON.toJSONString(rs);
		System.out.println(json);
	}
	
	private class ZookeeperPathEventListener implements PathChildrenCacheListener{

		@Override
		public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
			
			
		}
		
	}
	
}
