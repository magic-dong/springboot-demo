package com.lzd.learn.exception;

@SuppressWarnings("serial")
public class RedisLockException extends Exception{

	public RedisLockException(String msg) {
		super(msg);
	}

	public RedisLockException(String msg,Throwable cause) {
		super(msg,cause);
	}
}
