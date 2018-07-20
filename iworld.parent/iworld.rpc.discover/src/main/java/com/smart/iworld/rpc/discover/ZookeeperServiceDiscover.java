package com.smart.iworld.rpc.discover;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
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
		List<ServiceInfo> serviceInfos = ServicePathNodeCache.getServiceInfo(serviceName);
		if(serviceInfos != null && serviceInfos.size() >0) {
			return serviceInfos;
		}
		serviceInfos = new ArrayList<>();
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
			ServicePathNodeCache.setServiceInfo(servicePath, serviceInfos);
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
			List<String> serviceNodes = curator.getChildren().forPath(parentPath);
			for(String serviceNode : serviceNodes) {
				List<ServiceInfo> serviceInfos = new ArrayList<>();
				List<String> childrenNodes = curator.getChildren().forPath(serviceNode);
				for(String childrenNode : childrenNodes) {
					byte[] serviceData = curator.getData().forPath(childrenNode);
					String result = new String(serviceData, serverInfo.getCharSet());
					ServiceInfo serviceInfo = JSON.parseObject(result, ServiceInfo.class);
					serviceInfos.add(serviceInfo);
				}
				ServicePathNodeCache.setServiceInfo(serviceNode, serviceInfos);
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
		System.out.println(rs.getClass().getName());
		rs.setInterfaceName(rs.getClass().getName());
		rs.setPriority(1);
		String json = JSON.toJSONString(rs);
		System.out.println(json);
		String format = String.format("%s/%s", "ni", "ta");
		System.out.println(format);
	}

	private class ZookeeperPathEventListener implements PathChildrenCacheListener{

		@Override
		public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
			ChildData childData = event.getData();
			switch(event.getType()) {
			case CHILD_ADDED:
			case CHILD_UPDATED:
				dealChildEvent(childData);
				break;
			case CHILD_REMOVED:
				String path = childData.getPath();
				ServicePathNodeCache.deleteServiceInfo(path);
				break;
			default:
				disCoverAllService();
				break;
			}
		}

	}

	private void dealChildEvent(ChildData childData) {
		byte[] data = childData.getData();
		String charSet = serverInfo.getCharSet();
		String result = new String(data, Charset.forName(charSet));
		ServiceInfo serviceInfo = JSON.parseObject(result, ServiceInfo.class);
		String path = childData.getPath();
		ServicePathNodeCache.setServiceInfo(path, serviceInfo);
	}
}
