package com.smart.iworld.rpc.discover;
/**
 * 负载均衡开关
 * @author admin
 *
 */
public @interface EnableServiceLoadBalance {

	/***
	 * 实现负载均衡类名
	 * 全类名
	 * @return
	 */
	String loadBanlanceName() default "default";
	
	/***
	 * 实现负载均衡类
	 * 
	 * @return
	 */
	Class<?> loadBanlanceClass();
}
