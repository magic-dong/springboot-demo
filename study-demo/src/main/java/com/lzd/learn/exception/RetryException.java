package com.lzd.learn.exception;

@SuppressWarnings("serial")
public class RetryException extends Exception{

	public RetryException(String msg) {
		super(msg);
	}

	public RetryException(String msg,Throwable cause) {
		super(msg,cause);
	}
	
}
