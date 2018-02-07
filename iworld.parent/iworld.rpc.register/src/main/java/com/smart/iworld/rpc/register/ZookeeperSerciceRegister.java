package com.smart.iworld.rpc.register;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.smart.iworld.rpc.api.RegisterServiceInfo;
import com.smart.iworld.rpc.api.ServiceInfo;
import com.smart.iworld.rpc.api.ZookeeperServerInfo;


public class ZookeeperSerciceRegister implements IServiceRegister {

	private final Logger logger = LoggerFactory.getLogger(ZookeeperSerciceRegister.class);
	private ZooKeeper zooKeeper;

	private ZookeeperServerInfo serverInfo;

	public ZookeeperSerciceRegister(ZookeeperServerInfo serverInfo) {
		this.serverInfo = serverInfo;
	}

	public void start() {
		try {
			CountDownLatch cdl = new CountDownLatch(1);
			zooKeeper = new ZooKeeper(serverInfo.getConnetString(), serverInfo.getSessionTimeout(), new ZookeeperRegisterWatche(cdl));
			cdl.await();
			logger.debug("连接服务中心");
		} catch (Exception e) {
			logger.error("连接服务中心失败:{}", e);
		}
	}

	@Override
	public void registeService(RegisterServiceInfo registerServiceInfo) {

		try {
			ServiceInfo serviceInfo = new ServiceInfo();
			serviceInfo.setInterfaceName(registerServiceInfo.getInterfaceName());
			serviceInfo.setPriority(registerServiceInfo.getPriority());
			serviceInfo.setUrl(registerServiceInfo.getUrl());
			String parentPath = serverInfo.getParentPath();
			Stat rootStat = zooKeeper.exists(parentPath, false);
			if(rootStat == null) {
				zooKeeper.create(parentPath, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
			logger.debug(String.format("create path %s", parentPath));
			String servicePath = parentPath + "/" + registerServiceInfo.getInterfaceName();
			Stat serviceStat = zooKeeper.exists(servicePath, true);
			if(serviceStat != null) {
				String createServicePath = zooKeeper.create(parentPath + "/" + registerServiceInfo.getInterfaceName(), null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				logger.debug("create service path " + createServicePath);
			}
			String urlPath = servicePath + "/" +registerServiceInfo.getUrl() + ":" +registerServiceInfo.getPort();
			String registerData = JSON.toJSONString(serviceInfo);
			String createUrlPath = zooKeeper.create(urlPath, registerData.getBytes(serverInfo.getCharSet()), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			logger.debug("create urlPath success " + createUrlPath);
		} catch (Exception e) {
			logger.error("创建节点失败");
		}
	}

	private class ZookeeperRegisterWatche implements Watcher{

		private CountDownLatch cdl;
		
		public ZookeeperRegisterWatche(CountDownLatch cdl) {
			this.cdl = cdl;
		}
		
		@Override
		public void process(WatchedEvent event) {
			if(KeeperState.SyncConnected == event.getState()) {
				if(EventType.None == event.getType() && null == event.getPath()) {
					cdl.countDown();
				}
			}
		}

	}

}
