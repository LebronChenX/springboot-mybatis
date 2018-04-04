package com.lebron.springboot.config.datasource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.io.VFS;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
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
// 启动类一定不能扫描到BaseService
// @MapperScan(basePackages = {"com.lebron.springboot.mapper" })
@MapperScan(basePackages = "com.lebron.springboot.mapper")
public class MybatisConfiguration {

    // private static final Logger logger = LoggerFactory.getLogger(TxxsbatisConfiguration.class);

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
        // 解决myBatis下 不能嵌套jar文件的问题
        VFS.addImplClass(SpringBootVFS.class);
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(roundRobinDataSouceProxy());
        factory.setTypeAliasesPackage(mybatisProperties.getTypeAliasesPackage());
        factory.setMapperLocations(mapperLocations());
        if (this.interceptors != null && this.interceptors.length > 0) {
            factory.setPlugins(interceptors);
        }
        // 在setConfigLocation方法中指定了配置文件地址，不需要在添加setting
        factory.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        // TODO 为什么不打印sql
        factory.getObject().getConfiguration().setLogImpl(StdOutImpl.class);
        return factory.getObject();
    }

    @Bean
    public Resource[] mapperLocations() throws IOException {
        return new PathMatchingResourcePatternResolver().getResources(mybatisProperties.getMapperLocations());
    }

    /**
     * 有多少个数据源就要配置多少个bean
     */
    @Bean
    public AbstractRoutingDataSource roundRobinDataSouceProxy() {
        int size = Integer.parseInt(dataSourceSize);
        RoutingDataSource proxy = new RoutingDataSource(size);
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
        p.setProperty("dialect", "mysql");
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

}
