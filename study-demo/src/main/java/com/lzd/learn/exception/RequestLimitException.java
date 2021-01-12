package com.lzd.learn.exception;


/**
 * 限流异常处理类
 * 
 * @author lzd
 * @date 2019年7月16日
 * @version
 */
@SuppressWarnings("serial")
public class RequestLimitException extends Exception {

	public RequestLimitException() {
		this("Http请求超出时间范围内设定的限制次数，请稍后再试！");
	}

	public RequestLimitException(String msg) {
		super(msg);
	}

	public RequestLimitException(String msg,Throwable cause) {
		super(msg,cause);
	}
	
	public String defaultExceptionHandler() {
		return "Http请求超出时间范围内设定的限制次数，请稍后再试！";
	}
}
