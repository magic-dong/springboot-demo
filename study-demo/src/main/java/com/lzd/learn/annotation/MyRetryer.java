package com.lzd.learn.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyRetryer {
	
	/**
	 * 触发重试的异常类型
	 * @author lzd
	 * @date 2019年11月15日:下午2:34:53
	 * @return
	 * @description
	 */
	Class<? extends Throwable>[] value() default {};
	
	/**
	 * 最大重试次数
	 * @author lzd
	 * @date 2019年11月15日:下午2:34:37
	 * @return
	 * @description
	 */
	int maxAttempt() default 3;
	
	/**
	 * 延时delayMsec进行重试（单位时间默认：秒）
	 * @author lzd
	 * @date 2019年11月15日:下午2:35:10
	 * @return
	 * @description
	 */
	long delayMsec() default 1000;
	
	/**
	 * 等待waitMsec进行下一轮重试（单位时间默认：秒）
	 * @author lzd
	 * @date 2019年11月15日:下午2:36:13
	 * @return
	 * @description
	 */
	long waitMsec() default 2000;
}
