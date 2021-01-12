package com.lzd.learn.exception;


/**
 * 全局异常类
 * @author lzd
 * @date 2019年4月2日
 * @version
 */
@SuppressWarnings("serial")
public class GlobalDefaultException extends Exception{
	
	public GlobalDefaultException(){
		this("对不起，服务器繁忙，请稍后再试！");
	}
	
	public GlobalDefaultException(String msg){
		super(msg);
	}
	
	public GlobalDefaultException(String msg,Throwable cause){
		super(msg, cause);
	}
	
    public String defaultExceptionHandler(){
        return "对不起，服务器繁忙，请稍后再试！";
    }
	
}
