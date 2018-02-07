package com.smart.iworld.rpc.discover;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ProxyMethodIntercept implements MethodInterceptor {

	private ServicePathNodeCache nodeCache;
	
	public ProxyMethodIntercept(ServicePathNodeCache nodeCache) {
		this.nodeCache = nodeCache;
	}
	
	public Object intercept(Object arg0, Method arg1, Object[] arg2, MethodProxy arg3) throws Throwable {
		//ServicePathNodeCache cache = new ServicePathNodeCache(serviceDiscover)
		return null;
	}

}
