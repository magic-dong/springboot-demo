package com.lzd.learn.aspect;

import java.lang.reflect.Method;
import java.util.UUID;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.lzd.learn.annotation.MyRetryer;
import com.lzd.learn.annotation.RedisLock;
import com.lzd.learn.exception.RedisLockException;
import com.lzd.learn.exception.RetryException;
import com.lzd.learn.utils.RedisUtils;
import com.lzd.learn.utils.StringUtils;

/**
 * 分布式锁的切面
 * 当前版本缺陷:
 * 1.操作粒度不够小时（即业务操作时间超过过期时间），破坏了锁的互斥性。（可优化）
 *   业务操作时间超过过期时间，自动删除了锁，破坏了锁的互斥性。
 * 2.休眠并反复尝试加锁效率较低（可优化）
 *　   tryLock方法在客户端线程加锁失败后，会休眠一段时间之后再进行重试。当锁的持有者持有锁的时间很长时，
 *	  其它客户端会有大量无效的重试操作，造成系统资源的浪费。进一步优化时，可以使用发布订阅的方式。
 *	  这时加锁失败的客户端会监听锁被释放的信号，在锁真正被释放时才会进行新的加锁操作，从而避免不必要的轮询操作，以提高效率。
 * 3.主从同步可能导致锁的互斥性失效（困难）
 *　　在redis主从结构下，出于性能的考虑，redis采用的是主从异步复制的策略，这会导致短时间内主库和从库数据短暂的不一致。
 *　   试想，当某一客户端刚刚加锁完毕，redis主库还没有来得及和从库同步就挂了，之后从库中新选拔出的主库是没有对应锁记录的，
 *   这就可能导致多个客户端加锁成功，破坏了锁的互斥性。
 * 4.不是一个公平的锁（可优化）
 *　   当前实现版本中，多个客户端同时对锁进行抢占时，是完全随机的，既不遵循先来后到的顺序，客户端之间也没有加锁的优先级区别。
 *　   后续优化时可以提供一个创建公平锁的接口，能指定加锁的优先级，内部使用一个优先级队列维护加锁客户端的顺序。公平锁虽然效率稍低，
 *   但在一些场景能更好的控制并发行为
 * @author lzd
 * @date 2019年11月22日
 * @version
 */
@Aspect
@Component
@Order(4)
public class RedisLockAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisLockAspect.class);
	
	private static final ThreadLocal<String> REQUEST_ID_MAP=new ThreadLocal<>();
	
	@Autowired
	private RedisDistributedLock lock;
	
	/**
	 * 配置切点
	 * 
	 * @author lzd
	 * @date 2019年5月27日:下午5:57:03
	 * @description
	 */
	@Pointcut("@annotation(com.lzd.learn.annotation.RedisLock)")
	public void lockPointCut() {

	}

	@Around("lockPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable{
		
		try {
			MethodSignature methodSignature = (MethodSignature)point.getSignature();
	        Method method = methodSignature.getMethod();
	        RedisLock redisLock = method.getAnnotation(RedisLock.class);
	        if(redisLock!=null){
	        	//加锁
	        	boolean tryLock = lock.tryLock(redisLock);
	        	if(tryLock){
	        		Object result = point.proceed();
	        		//解锁
	        		lock.releseLock(redisLock);
	        		return result;
	        	}
	        }
	        String lockLog=point.getTarget().getClass().getCanonicalName().concat(".").concat(method.getName());
	        String msg=String.format("当前请求执行%s方法加锁失败！", lockLog);
	        throw new RedisLockException(msg);
		}catch (Throwable e) {
			// TODO: handle exception
			throw e;
		}
		
        
	}
	
	/**
	 * 内部类使重试切面生成代理类生效重试机制
	 * （优化可以将锁封装成api接口）
	 * @author lzd
	 * @date 2019年11月25日
	 * @version
	 */
	@Component
	static class RedisDistributedLock{
		
		/**
		 * 加锁
		 * @author lzd
		 * @date 2019年11月22日:下午1:53:13
		 * @param redisLock
		 * @return
		 * @throws Exception
		 * @description
		 */
		@MyRetryer(value=RetryException.class,maxAttempt=3,delayMsec=1000,waitMsec=2000)
		public boolean  tryLock(RedisLock redisLock) throws Exception{
			//客户端requestId
			String requestId = REQUEST_ID_MAP.get();
			//锁的key
			String lockKey = redisLock.lockKey();
			if(StringUtils.isNull(lockKey)){
				throw new RedisLockException("redis分布式锁的key不能为空！");
			}
			//过期时间
			long expireTime = redisLock.expireTime();
	    	if(StringUtils.isNotNull(requestId)){
	    		//当前线程 已经存在requestId 重入
	    		boolean tryGetDistibutedLock = RedisUtils.tryGetDistibutedLock(lockKey, requestId, expireTime);
	    		if(tryGetDistibutedLock){
	    			logger.info("重入加锁成功，requestId设置为:{}!",requestId);
	    			return tryGetDistibutedLock;
	    		}
	    	}else{
	    		//当前线程 不存在requestId
	    		String newRequestId=UUID.randomUUID().toString().replaceAll("-", "");
	    		boolean tryGetDistibutedLock = RedisUtils.tryGetDistibutedLock(lockKey, newRequestId, expireTime);
	    		if(tryGetDistibutedLock){
	    			//加锁成功，设置新的requestId
	    			REQUEST_ID_MAP.set(newRequestId);
	    			logger.info("加锁成功，requestId设置为:{}!",newRequestId);
	    			return tryGetDistibutedLock;
	    		}
	    	}
	    	//加锁未成功，重试几次
	    	throw new RetryException("加锁重试！");
		}
		
		/**
		 * 解锁
		 * @author lzd
		 * @date 2019年11月22日:下午1:58:24
		 * @param redisLock
		 * @description
		 */
		public void releseLock(RedisLock redisLock){
			//客户端requestId
			String requestId = REQUEST_ID_MAP.get();
			//锁的key
			String lockKey = redisLock.lockKey();
			if(StringUtils.isNotNull(requestId)){
				boolean releaseDistributedLock = RedisUtils.releaseDistributedLock(lockKey, requestId);
				if(releaseDistributedLock){
					//移除ThreadLocal中的数据
					REQUEST_ID_MAP.remove();
					logger.info("解锁成功，requestId设置为:{}!",requestId);
				}
			}
		}
		
	}

}
