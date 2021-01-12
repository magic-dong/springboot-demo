package com.lzd.learn.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.lzd.learn.datasource.DynamicDataSource;
import com.lzd.learn.enums.DataSourceTypeEnum;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * Druid配置多数据源
 * 
 * @author lzd
 * @date 2019年5月22日
 * @version
 */
@Configuration
public class DruidConfig {

	@Bean
	@ConfigurationProperties("spring.datasource.druid.slave")
	@ConditionalOnProperty(prefix = "spring.datasource.druid.slave", name = "enabled", havingValue = "true")
	public DataSource oracleDataSource() {
		return DruidDataSourceBuilder.create().build();
	}
	
	@Bean
	@ConfigurationProperties("spring.datasource.druid.master")
	public DataSource mysqlDataSource() {
		return DruidDataSourceBuilder.create().build();
	}
	
	@Bean(name = "dynamicDataSource")
	@Primary
	public DynamicDataSource dataSource(DataSource oracleDataSource, DataSource mysqlDataSource) {
		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put(DataSourceTypeEnum.ORACLE.name(), oracleDataSource);
		targetDataSources.put(DataSourceTypeEnum.MYSQL.name(), mysqlDataSource);
		return new DynamicDataSource(mysqlDataSource, targetDataSources);
	}
	
}
