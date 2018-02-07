package com.smart.iworld.rpc.api.exception;

public class ServiceNotFoundException extends Exception {

	private static final long serialVersionUID = 8480738576682432377L;

	public ServiceNotFoundException() {
		super();
	}

	public ServiceNotFoundException(String message) {
		super(message);
	}

	public ServiceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceNotFoundException(Throwable cause) {
		super(cause);
	}
	
}
