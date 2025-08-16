package com.smart.iworld.rpc.discover;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class ProxyMethodIntercept implements MethodInterceptor {

	private String parentPath;
	
	public ProxyMethodIntercept(String parentPath) {
		this.parentPath = parentPath;
	}
	
	@Override
	public Object intercept(Object arg0, Method arg1, Object[] arg2, MethodProxy arg3) throws Throwable {
		arg0.getClass().getName();
		String serviePath = String.format("%s/%s", parentPath, arg0.getClass().getName());
		
		return null;
	}

}
