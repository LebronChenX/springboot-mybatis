package com.lebron.springboot.config.datasource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.github.abel533.mapperhelper.MapperInterceptor;
import com.github.pagehelper.PageHelper;
import com.lebron.springboot.config.mybatis.MyMybatisProperties;

@Configuration
@ConditionalOnClass({EnableTransactionManagement.class })
@Import({DataBaseConfiguration.class })
//@MapperScan(basePackages = {"com.hui.readwrite.mapper.master1" })
//启动类一定不能扫描到BaseService
@MapperScan(basePackages = "com.lebron.springboot.mapper")
public class TxxsbatisConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(TxxsbatisConfiguration.class);

    @Value("${spring.datasource.type}")
    private Class<? extends DataSource> dataSourceType;

    @Value("${datasource.readSize}")
    private String dataSourceSize;

    @Autowired
    @Qualifier("writeDataSource")
    private DataSource dataSource;

    @Autowired
    @Qualifier("readDataSources")
    private List<DataSource> readDataSources;

    @Autowired(required = false)
    private Interceptor[] interceptors;

    @Autowired
    private MyMybatisProperties mybatisProperties;

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(roundRobinDataSouceProxy());
        factory.setTypeAliasesPackage(mybatisProperties.getTypeAliasesPackage());
        factory.setMapperLocations(mapperLocations());
        if (this.interceptors != null && this.interceptors.length > 0) {
            factory.setPlugins(interceptors);
        }
        // 在setConfigLocation方法中指定了配置文件地址，不需要在添加setting
        factory.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        //TODO 为什么报错
//         factory.getObject().getConfiguration().setLogImpl(log());
        factory.getObject().getConfiguration().setLogPrefix("logImpl");

//        factory.setConfigLocation(configLocation());
        return factory.getObject();
    }

    @Bean
    public Resource[] mapperLocations() throws IOException {
        return new PathMatchingResourcePatternResolver().getResources(mybatisProperties.getMapperLocations());
    }

    @Bean
    public Resource configLocation() {
        return new PathResource("/mybatis/mybatis-config.xml");
//        return new PathResource(mybatisProperties.getConfigLocation());
    }

    /**
     * 有多少个数据源就要配置多少个bean
     */
    @Bean
    public AbstractRoutingDataSource roundRobinDataSouceProxy() {
        int size = Integer.parseInt(dataSourceSize);
        TxxsAbstractRoutingDataSource proxy = new TxxsAbstractRoutingDataSource(size);
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        targetDataSources.put(DataSourceType.write.getType(), dataSource);
        for (int i = 0; i < size; i++) {
            targetDataSources.put(i, readDataSources.get(i));
        }
        proxy.setDefaultTargetDataSource(dataSource);
        proxy.setTargetDataSources(targetDataSources);
        return proxy;
    }

    @Bean
    public PageHelper pageHelper(DataSource dataSource) {
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }

    @Bean
    public MapperInterceptor mapperInterceptor() {
        MapperInterceptor mapperInterceptor = new MapperInterceptor();
        Properties p = new Properties();
        p.setProperty("UUID", "MYSQL");
        p.setProperty("mappers", "com.github.abel533.mapper.Mapper");
        mapperInterceptor.setProperties(p);
        return mapperInterceptor;
    }

    @Bean
    public Log log() {
        Log log = new StdOutImpl("STDOUT_LOGGING");
        return log;
    }
}
