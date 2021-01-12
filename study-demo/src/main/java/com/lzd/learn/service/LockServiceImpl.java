package com.lzd.learn.service;

import org.springframework.stereotype.Service;

import com.lzd.learn.annotation.RedisLock;

@Service
public class LockServiceImpl implements LockService {

	@Override
	@RedisLock(lockKey="lockKey",expireTime=10000)
	public String method1() throws Exception{
		// TODO Auto-generated method stub
		System.out.println("11");
		return "method1";
	}
}
