package com.lzd.learn.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestLimit
@RequestMapping(method = RequestMethod.GET)
public @interface GetLimitMapping {
	/**
	 * Alias for {@link RequestMapping#name}.
	 */
	@AliasFor(annotation = RequestMapping.class)
	String name() default "";

	/**
	 * Alias for {@link RequestMapping#value}.
	 */
	@AliasFor(annotation = RequestMapping.class)
	String[] value() default {};

	/**
	 * Alias for {@link RequestMapping#path}.
	 */
	@AliasFor(annotation = RequestMapping.class)
	String[] path() default {};

	/**
	 * Alias for {@link RequestMapping#params}.
	 */
	@AliasFor(annotation = RequestMapping.class)
	String[] params() default {};

	/**
	 * Alias for {@link RequestMapping#headers}.
	 */
	@AliasFor(annotation = RequestMapping.class)
	String[] headers() default {};

	/**
	 * Alias for {@link RequestMapping#consumes}.
	 * @since 4.3.5
	 */
	@AliasFor(annotation = RequestMapping.class)
	String[] consumes() default {};

	/**
	 * Alias for {@link RequestMapping#produces}.
	 */
	@AliasFor(annotation = RequestMapping.class)
	String[] produces() default {};
	
	/**
	 * 请求次数指定时间范围,默认为秒（Redis数据过期时间）
	 * Alias for {@link RequestLimit#second}
	 * @author lzd
	 * @date 2019年7月16日:下午3:40:59
	 * @return
	 * @description
	 */
	@AliasFor(annotation = RequestLimit.class,attribute="second")
	int second() default  10;
	
	/**
	 * 指定second时间内，API请求次数
	 * Alias for {@link RequestLimit#counter}
	 * @author lzd
	 * @date 2019年7月16日:下午3:40:00
	 * @return
	 * @description
	 */
	@AliasFor(annotation = RequestLimit.class,attribute="counter")
	int counter() default 20;
}
