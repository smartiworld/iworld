package com.smart.iworld.rpc.discover;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class ServiceProxyFactory {

	@SuppressWarnings("unchecked")
	public static <T> T createProxyBean(Class<T> interfaceClass, MethodInterceptor methodInterceptor) {
		Enhancer enhance = new Enhancer();
		enhance.setSuperclass(interfaceClass);
		enhance.setCallback(methodInterceptor);
		return (T)enhance.create();
	}
}
