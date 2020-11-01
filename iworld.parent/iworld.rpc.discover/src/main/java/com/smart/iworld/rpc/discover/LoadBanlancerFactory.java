package com.smart.iworld.rpc.discover;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;

public class LoadBanlancerFactory {
	
	private static ConcurrentMap<String, LoadBanlancer> loadBanlancerMap = new ConcurrentHashMap<>();
	
	private static ReentrantReadWriteLock loadBanlancerLock = new ReentrantReadWriteLock();
	
	private static final String DEFAULT_LOADNAME = "default";
	
	private static String loadBalanceName;
	
	private boolean isSupport = false;

	public static LoadBanlancer getLoadBanlancer(String name) {
		ReadLock readLock = loadBanlancerLock.readLock();
		try {
			readLock.lock();
			if(name == null || "".equals(name)) {
				name = DEFAULT_LOADNAME;
			}
			LoadBanlancer loadBanlancer = loadBanlancerMap.get(name);
			if(loadBanlancer == null && "default".equals(name)) {
				loadBanlancer = new LoadBanlancer();
			}
			return loadBanlancer;
		} finally {
			readLock.unlock();
		}
	}
	
	public static LoadBanlancer getLoadBanlancer() {
		ReadLock readLock = loadBanlancerLock.readLock();
		try {
			readLock.lock();
			if(loadBalanceName == null || "".equals(loadBalanceName)) {
				loadBalanceName = DEFAULT_LOADNAME;
			}
			LoadBanlancer loadBanlancer = loadBanlancerMap.get(loadBalanceName);
			if(loadBanlancer == null && "default".equals(loadBalanceName)) {
				loadBanlancer = new LoadBanlancer();
			}
			return loadBanlancer;
		} finally {
			readLock.unlock();
		}
	}
}
