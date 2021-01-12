package com.lzd.learn.aspect;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.lzd.learn.annotation.MyRetryer;
import com.lzd.learn.exception.RetryException;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;

/**
 * 方法重试切面
 * @author lzd
 * @date 2019年11月15日
 * @version
 */
@Aspect
@Component
@Order(3)
public class MyRetryerAspect {
	
	protected static final Logger logger = LoggerFactory.getLogger(MyRetryerAspect.class);
	protected static final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Pointcut("@annotation(com.lzd.learn.annotation.MyRetryer)")
	public void pointCut(){
		
	}
	
	@Around("pointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable{
		try {
			return point.proceed();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(),e);
			MethodSignature signature = (MethodSignature) point.getSignature();
			Method method = signature.getMethod();
			MyRetryer myRetryer = method.getAnnotation(MyRetryer.class);
			
		    RetryerBuilder<Object> retryerBuilder = RetryerBuilder.<Object>newBuilder();
		    //延迟时间设置
//		    if (myRetryer.delayMsec()>0) {
//		    	//withStopStrategy重复设置StopStrategy会报异常
//		    	retryerBuilder.withStopStrategy(StopStrategies.stopAfterDelay(myRetryer.delayMsec(), TimeUnit.MILLISECONDS));
//	        }
		    if(myRetryer.delayMsec()<=0){
				logger.info("延迟时间设置为小于0！");
				throw new RetryException("重试配置异常：延迟时间设置小于0！");
			}else{
				try {
					Thread.sleep(myRetryer.delayMsec());
				} catch (Exception e2) {
					// TODO: handle exception
					logger.error(e.getMessage(),e);
				}
			}
		    //重试次数设置
		    if(myRetryer.maxAttempt()<=0){
		    	logger.info("重试次数设置为小于0！");
		    	throw new RetryException("重试配置异常：重试次数设置小于0！");
		    }else{
		    	retryerBuilder.withStopStrategy(StopStrategies.stopAfterAttempt(myRetryer.maxAttempt()));
		    }
		    //等待时间设置
		    if(myRetryer.waitMsec()<=0){
		    	logger.info("等待时间间隔设置为小于0！");
		    	throw new RetryException("重试配置异常：等待时间间隔设置小于0！");
		    }else{
		    	retryerBuilder.withWaitStrategy(WaitStrategies.fixedWait(myRetryer.waitMsec(), TimeUnit.MILLISECONDS));
		    }
		    //异常类型设置
		    if(myRetryer.value().length<=0){
		    	logger.info("异常类型设置为小于0！");
		    	throw new RetryException("重试配置异常：异常类型设置小于0！");
		    }else{
		    	for (Class<? extends Throwable> retryThrowable : myRetryer.value()) {
	                if (retryThrowable != null && Throwable.class.isAssignableFrom(retryThrowable)) {
	                	retryerBuilder.retryIfExceptionOfType(retryThrowable);
	                }
	            }
		    }
		    //重试方法
		    String retryLog=point.getTarget().getClass().getCanonicalName().concat(".").concat(method.getName());
		    return retryerBuilder.build().call(new Callable<Object>() {
				int count=0;
				
				@Override
				public Object call() throws Exception {
					// TODO Auto-generated method stub
					try {
						++count;
						logger.warn("At the time:{} Retryer will {} times retry the method:{}.",sdf.format(new Date()),count,retryLog);
						return point.proceed();
					} catch (Throwable throwable) {
						// TODO: handle exception
						if(throwable instanceof Exception){
							throw (Exception)throwable;
						}else{
							throw new Exception(throwable);
						}
					}
				}
		   });
		}
	}
}


