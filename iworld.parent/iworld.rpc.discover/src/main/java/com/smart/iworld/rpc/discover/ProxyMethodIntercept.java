package com.smart.iworld.rpc.discover;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ProxyMethodIntercept implements MethodInterceptor {

	private String parentPath;
	
	public ProxyMethodIntercept(String parentPath) {
		this.parentPath = parentPath;
	}
	
	public Object intercept(Object arg0, Method arg1, Object[] arg2, MethodProxy arg3) throws Throwable {
		arg0.getClass().getName();
		String serviePath = String.format("%s/%s", parentPath, arg0.getClass().getName());
		ServicePathNodeCache.getServiceInfo(serviePath);
		arg1.getParameterTypes();
		arg1.getParameters();
		return null;
	}

}
