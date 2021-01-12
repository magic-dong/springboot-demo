package com.lzd.learn.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.lzd.learn.annotation.DataSource;
import com.lzd.learn.datasource.DynamicDataSourceContextHolder;
import com.lzd.learn.utils.StringUtils;

/**
 * 多数据源处理
 * 注意：切面里面使用@Order()指定优先级的时候，值越小，优先级越高。
 * 但不能比ExposeInvocationInterceptor等类（默认为Ordered.HIGHEST_PRECEDENCE+1）
 * 优先级还要高，此类作用于方法等过滤拦截；
 * 否则抛异常java.lang.IllegalStateException:No MethodInvocation found..... 
 * @author lzd
 * @date 2019年5月17日
 * @version
 */
@Aspect
@Component
@Order(1)
public class DataSourceAspect
{
    protected Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * 配置切点
     * @author lzd
     * @date 2019年5月27日:下午5:57:03
     * @description
     */
    @Pointcut("(execution(* com.lzd.learn.dao..**.*(..)))")
    public void dsPointCut()
    {

    }

    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable
    {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        DataSource dataSource = method.getAnnotation(DataSource.class);
        
    	//获取切点方法所在声明类或者接口上的注解
        if(StringUtils.isNull(dataSource)){
        	dataSource=method.getDeclaringClass().getAnnotation(DataSource.class);
        }
        
        if (StringUtils.isNotNull(dataSource))
        {
            DynamicDataSourceContextHolder.setDateSoureType(dataSource.value().name());
        }

        try
        {
            return point.proceed();
        }
        finally
        {
            // 销毁数据源 在执行方法之后
            DynamicDataSourceContextHolder.clearDateSoureType();
        }
    }
}
