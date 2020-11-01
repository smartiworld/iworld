package com.smart.iworld.rpc.api;

public abstract class AbstractServiceFinder implements ServiceFinder {

	private ILoadBanlance iLoadBanlance;
	
	@Override
	public void setLoadBanlance(ILoadBanlance iLoadBanlance) {
		this.iLoadBanlance = iLoadBanlance;
	}
	
	@Override
	public ILoadBanlance getLoadBanlance() {
		return iLoadBanlance;
	}
}
