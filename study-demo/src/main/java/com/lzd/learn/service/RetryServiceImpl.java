package com.lzd.learn.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.lzd.learn.annotation.MyRetryer;

@Service
public class RetryServiceImpl implements RetryService {
	
	private int counts=0;
	private SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss S");
	
	@Retryable(value=Exception.class,maxAttempts=5,backoff=@Backoff(delay=1000,multiplier=2))
	@Override
	public String testSpringRetry(Map<String,Object> param) throws Exception{
		System.out.println("参数"+param);
		++counts;
		System.out.println("counts====="+counts+" at "+sdf.format(new Date()));
		throw new RuntimeException("重试异常！");
	}
	
	@Recover
	public String  recover(RuntimeException e){
		counts=0;
		return "1======="+e.getMessage();
	}
	
	@Recover
	public String  recover(Exception e){
		counts=0;
		return "2======="+e.getMessage();
	}

	@MyRetryer(value=RetryException.class,maxAttempt=3,delayMsec=3000,waitMsec=2000)
	@Override
	public String testGuavaRetry(Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		//定义重试机制
		System.out.println("参数"+param);
		++counts;
		System.out.println("counts====="+counts+" at "+sdf.format(new Date()));
		throw new RetryException("触发重试！");
	}
}
