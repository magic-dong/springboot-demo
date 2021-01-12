package com.lzd.learn.aspect;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lzd.learn.annotation.RequestLimit;
import com.lzd.learn.exception.GlobalDefaultException;
import com.lzd.learn.exception.RequestLimitException;
import com.lzd.learn.json.MessageResult;
import com.lzd.learn.utils.IpUtils;
import com.lzd.learn.utils.RedisUtils;
import com.lzd.learn.utils.StringUtils;

/**
 * 接口限流切面
 * 注意：切面里面使用@Order()指定优先级的时候，值越小，优先级越高。
 * 但不能比ExposeInvocationInterceptor等类（默认为Ordered.HIGHEST_PRECEDENCE+1）
 * 优先级还要高，此类作用于方法等过滤拦截；
 * 否则抛异常java.lang.IllegalStateException:No MethodInvocation found.....
 * @author lzd
 * @date 2019年7月17日
 * @version
 */
@Aspect
@Component
@Order(2)
public class RequestLimitAspect {

	private static final Logger logger = LoggerFactory.getLogger(RequestLimitAspect.class);

	/**
	 * Redis缓存中key的前缀
	 */
	private final static String PREFIX = "req_limit_";

	/**
	 * 配置切点1
	 * @author lzd
	 * @date 2019年5月27日:下午5:57:03
	 * @description
	 */
	@Pointcut("@annotation(com.lzd.learn.annotation.RequestLimit)")
	public void requestLimitPointCut() {

	}
	
	/**
	 * 配置切点2
	 * @author lzd
	 * @date 2019年5月27日:下午5:57:03
	 * @description
	 */
	@Pointcut("@annotation(com.lzd.learn.annotation.GetLimitMapping)")
	public void getLimitMappingPointCut() {

	}

	/**
	 * 综合配置切点3
	 * @author lzd
	 * @date 2019年12月2日:下午5:59:47
	 * @description
	 */
	@Pointcut("requestLimitPointCut() || getLimitMappingPointCut()")
	public void enhancePointCut() {

	}

	@Around("enhancePointCut()")
	public Object around(ProceedingJoinPoint point){
		try {
			MethodSignature signature = (MethodSignature) point.getSignature();
			Method method = signature.getMethod();
//			RequestLimit requestLimit = method.getAnnotation(RequestLimit.class);
			//可获取组合注解中的目标注解
			RequestLimit requestLimit = AnnotatedElementUtils.findMergedAnnotation(method, RequestLimit.class);
			System.out.println(requestLimit);
			if (requestLimit!=null){
				ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				HttpServletRequest request = attributes.getRequest();
				String url = request.getRequestURL().toString();
				String ip = IpUtils.getIpAddr(request);
				String key = PREFIX.concat(ip).concat(":").concat(url);
				//获取注解配置的最大次数（默认：20次）
				Integer maxCounter=requestLimit.counter();
				//获取注解配置的过期时间 （默认：10秒）
				Integer second=requestLimit.second();
				
				//获取缓存里该时间范围内目前的计数值
				String nowCounter = String.valueOf(RedisUtils.get(key));
				Integer counter =StringUtils.isNotNull(nowCounter)?Integer.valueOf(nowCounter):null;
				if (counter == null) {
					//将key对应的值存入缓存中，并设置过期时间为second
					RedisUtils.set(key, 1, second);
					return point.proceed();
				}else if(counter < maxCounter){
					//将key对应的值递增加1存入缓存中,并设置过期时间为second
					RedisUtils.set(key, counter+1,second);
					return point.proceed();
				}else {
					logger.info("用户IP[" + ip + "]访问地址[" + url + "]超过了限定的次数[" + maxCounter + "]");
					throw new RequestLimitException();
				}
		    }
		}catch (RequestLimitException e) {
			// TODO: handle exception
			logger.error(e.getMessage());
			return MessageResult.error(e.getMessage());
		}catch (Throwable  e) {
			// TODO: handle exception
			logger.error(e.getMessage(),e);
			String defaultExceptionHandler = new GlobalDefaultException().defaultExceptionHandler();
			return MessageResult.error(defaultExceptionHandler);
		}
		return null;
	}
}
