package com.lzd.learn.aspect;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONArray;
import com.lzd.learn.annotation.PreventDuplicateSubmission;
import com.lzd.learn.json.MessageResult;
import com.lzd.learn.utils.Md5Utils;
import com.lzd.learn.utils.RedisUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 防止重复提交切面
 * 
 * @author lzd
 * @date 2021年1月11日
 * @version
 */
@Aspect
@Component
@Order(5)
@Slf4j
public class PreventDuplicateSubmissionAspect {

	/**
	 * 切点
	 * 
	 * @author lzd
	 * @date 2021年1月11日:下午4:05:10
	 * @param preventDuplicateSubmission
	 * @description
	 */
	@Pointcut("execution (* com.lzd.learn.controller..*.*(..)) && @annotation(com.lzd.learn.annotation.PreventDuplicateSubmission)")
	public void preventDuplicateSubmissionPointCut() {

	}

	@Around(value = "preventDuplicateSubmissionPointCut()")
	public Object preventDuplicateSubmissionPointAround(ProceedingJoinPoint point) throws Throwable{
		MethodSignature methodSignature = (MethodSignature)point.getSignature();
        Method method = methodSignature.getMethod();
        PreventDuplicateSubmission preventDuplicateSubmission = method.getAnnotation(PreventDuplicateSubmission.class);
		// 获取切面传入参数 这里是http请求参数
		Object[] args = point.getArgs();
		// 同一路径
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	    HttpServletRequest request = attributes.getRequest();
		String lockKeyParam = request.getRequestURI();
		if (args != null) {
			List<Object> list = Arrays.asList(args);
			JSONArray jsonArray = new JSONArray(list);
			// 同一参数 由于demo中请求的参数包含userId信息 这里同一参数中已经包含同一用户
			// 这里如果有自己的用户信息可以自己拼装来保证同一用户
			lockKeyParam += jsonArray.toJSONString();
		}
		String lockKey = Md5Utils.hash(lockKeyParam);
		String requestId = UUID.randomUUID().toString().replaceAll("-", "");
		
		//请求提交的处理时间（默认单位：毫秒）
		long submissionHandleTime = preventDuplicateSubmission.submissionHandleTime();
		boolean lock=RedisUtils.tryGetDistibutedLock(lockKey, requestId, submissionHandleTime);
		if (!lock) {
			// 没有获取到锁 请求正在处理中
			log.info("没有获取到锁 请求正在处理中");
			return MessageResult.success("请求正在处理中");
		}
		try {
			return point.proceed();
		}finally {
			RedisUtils.releaseDistributedLock(lockKey, requestId);
		}
	}

}
