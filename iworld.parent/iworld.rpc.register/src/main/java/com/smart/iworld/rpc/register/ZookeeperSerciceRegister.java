package com.smart.iworld.rpc.register;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.smart.iworld.rpc.api.RegisterServerInfo;
import com.smart.iworld.rpc.api.RegisterServiceInfo;


public class ZookeeperSerciceRegister implements IServiceRegister {

	private final Logger logger = LoggerFactory.getLogger(ZookeeperSerciceRegister.class);
	private ZooKeeper zooKeeper;

	private ZookeeperRegisterServerInfo registerServerInfo;

	public ZookeeperSerciceRegister(ZookeeperRegisterServerInfo registerServerInfo) {
		this.registerServerInfo = registerServerInfo;
	}

	public void start() {
		try {
			zooKeeper = new ZooKeeper(registerServerInfo.getConnetString(), registerServerInfo.getSessionTimeout(), new ZookeeperRegisterWatche());
			logger.debug("连接服务中心");
		} catch (IOException e) {
			logger.error("连接服务中心失败:{}", e);
		}
	}

	@Override
	public void registeService(RegisterServiceInfo registerServiceInfo) {

		try {
			String parentPath = registerServerInfo.getParentPath();
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
			String urlPath = servicePath + "/" +registerServiceInfo.getUrl();
			String registerData = JSON.toJSONString(registerServiceInfo);
			String createUrlPath = zooKeeper.create(urlPath, registerData.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			logger.debug("create urlPath success " + createUrlPath);
		} catch (Exception e) {
			logger.error("创建节点失败");
		}
	}

	private class ZookeeperRegisterWatche implements Watcher{

		@Override
		public void process(WatchedEvent event) {

		}

	}

}
