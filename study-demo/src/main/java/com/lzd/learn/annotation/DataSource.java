package com.lzd.learn.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.lzd.learn.enums.DataSourceTypeEnum;

/**
 * 自定义多数据源切换注解
 * (注意：可以参考苞米豆开源的https://github.com/baomidou/dynamic-datasource-spring-boot-starter使用seata升级解决跨库事务)
 * @author lzd
 * @date 2019年1月11日
 * @version
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource
{
    /**
     * 切换数据源名称
     */
    DataSourceTypeEnum value() default DataSourceTypeEnum.MYSQL;
}
