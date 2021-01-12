package com.lzd.learn.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * redis分布式锁
 * @author lzd
 * @date 2019年11月22日
 * @version
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {
  
	/**
	 * 分布式锁的key
	 * @author lzd
	 * @date 2019年11月22日:上午11:43:33
	 * @return
	 * @description
	 */
	String lockKey();
	
	/**
	 * 锁的过期时间（默认单位：毫秒）
	 * @author lzd
	 * @date 2019年11月22日:上午11:44:50
	 * @return
	 * @description
	 */
	long  expireTime() default 5000;
	
	
	/**
	 * 加锁重试次数
	 * @author lzd
	 * @date 2019年11月22日:上午11:46:23
	 * @return
	 * @description
	 */
	int  retryCount() default 3;
	
}
