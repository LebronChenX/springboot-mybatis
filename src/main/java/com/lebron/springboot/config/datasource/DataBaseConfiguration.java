package com.lebron.springboot.config.datasource;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;
import com.lebron.springboot.config.druid.DataSourceConfigBuilder;
import com.lebron.springboot.config.druid.ReadOneDataSourceBuilder;
import com.lebron.springboot.config.druid.WriteDataSourceBuilder;

@Configuration
public class DataBaseConfiguration {

    @Value("${spring.datasource.type}")
    private Class<? extends DataSource> dataSourceType;

    @Autowired
    private WriteDataSourceBuilder writeDataSourceBuilder;
    @Autowired
    private ReadOneDataSourceBuilder readOneDataSourceBuilder;
    @Autowired
    private DataSourceConfigBuilder dataSourceConfigBuilder;

    @Bean(name = "writeDataSource", destroyMethod = "close", initMethod = "init")
    @Primary
    public DataSource writeDataSource() {
        // return DataSourceBuilder.create().type(dataSourceType).build();
        DruidDataSource dataSource = writeDataSourceBuilder.createDruidDataSource();
        return dataSourceConfigBuilder.config(dataSource);
    }

    /**
     * 有多少个从库就要配置多少个
     * 
     * @return
     */
    @Bean(name = "readDataSourceOne")
    public DataSource readDataSourceOne() {
        // return DataSourceBuilder.create().type(dataSourceType).build();
        DruidDataSource dataSource = readOneDataSourceBuilder.createDruidDataSource();
        return dataSourceConfigBuilder.config(dataSource);
    }

    @Bean("readDataSources")
    public List<DataSource> readDataSources() {
        List<DataSource> dataSources = new ArrayList<DataSource>();
        dataSources.add(readDataSourceOne());
        return dataSources;
    }
}
