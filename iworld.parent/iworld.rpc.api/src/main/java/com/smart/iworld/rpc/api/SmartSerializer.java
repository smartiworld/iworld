package com.smart.iworld.rpc.api;


public interface SmartSerializer  {

	/**
	 * 序列化
	 * @param data
	 * @return
	 */
	public byte[] serializer(Object data);
	
	/**
	 * 反序列化
	 * @param datas
	 * @param resultType
	 * @return
	 */
	public <T> T deSerializer(byte[] datas, Class<T> resultType);
}
