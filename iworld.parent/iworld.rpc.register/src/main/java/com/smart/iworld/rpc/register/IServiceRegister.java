package com.smart.iworld.rpc.register;

import com.smart.iworld.rpc.api.RegisterServiceInfo;

/**
 * 服务注册接口
 *
 */
public interface IServiceRegister {
	
	/**
	 * 注册服务到管理服务中心,将接口实现注册到注册中心
	 * 
	 * @param registerServerInfo   注册中心
	 * @retuen void
	 */
	public void registeService(RegisterServiceInfo registerServiceInfo);
}
