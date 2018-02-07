package com.smart.iworld.rpc.api.exception;

public class RcpException extends Exception {

	private static final long serialVersionUID = 5432150002871843161L;


	public RcpException() {
		super();
	}

	public RcpException(String message) {
		super(message);
	}

	public RcpException(String message, Throwable cause) {
		super(message, cause);
	}

	public RcpException(Throwable cause) {
		super(cause);
	}

}
