package com.lzd.learn.service;

import java.util.Map;

public interface RetryService {
	
	 String testSpringRetry(Map<String,Object> param) throws Exception;
	 
	 String testGuavaRetry(Map<String,Object> param) throws Exception;
	 
}
