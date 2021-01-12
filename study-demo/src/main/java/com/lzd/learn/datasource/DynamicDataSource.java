package com.lzd.learn.datasource;

import java.util.Map;

import javax.sql.DataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 * @author lzd
 * @date 2019年5月19日
 * @version
 */
public class DynamicDataSource extends AbstractRoutingDataSource
{
    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources)
    {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey()
    {
        return DynamicDataSourceContextHolder.getDateSoureType();
    }
}