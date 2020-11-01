package com.smart.iworld.rpc.discover;

import net.sf.cglib.proxy.Enhancer;

public class ServiceProxyFactory {

	@SuppressWarnings("unchecked")
	public static <T> T createProxyBean(Class<T> interfaceClass, String parentPath) {
		ProxyMethodIntercept methodInterceptor = new ProxyMethodIntercept(parentPath);
		Enhancer enhance = new Enhancer();
		enhance.setSuperclass(interfaceClass);
		enhance.setCallback(methodInterceptor);
		return (T)enhance.create();
	}
}
