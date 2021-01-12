package com.lzd.learn.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 防止重复提交
 * @author lzd
 * @date 2021年1月11日
 * @version
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreventDuplicateSubmission {
	
	/**
	 * 请求提交的处理时间（默认单位：毫秒），也既锁的过期时间
	 * @author lzd
	 * @date 2019年11月22日:上午11:44:50
	 * @return
	 * @description
	 */
	long  submissionHandleTime() default 5000;
}
