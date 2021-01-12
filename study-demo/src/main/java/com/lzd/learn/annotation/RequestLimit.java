package com.lzd.learn.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 接口请求限流
 * @author lzd
 * @date 2019年7月16日
 * @version
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestLimit {
	
	/**
	 * 指定second时间内，API请求次数
	 * @author lzd
	 * @date 2019年7月16日:下午3:40:00
	 * @return
	 * @description
	 */
	int counter() default 20;
	
	/**
	 * 请求次数指定时间范围,默认为秒（Redis数据过期时间）
	 * @author lzd
	 * @date 2019年7月16日:下午3:40:59
	 * @return
	 * @description
	 */
	int second() default  10;
}
