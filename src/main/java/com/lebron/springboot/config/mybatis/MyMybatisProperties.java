package com.lebron.springboot.config.mybatis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = MyMybatisProperties.MYBATIS_PREFIX)
@PropertySource("classpath:mybatis.properties")
public class MyMybatisProperties {

    public static final String MYBATIS_PREFIX = "mybatis";

    private String mapperLocations;
    private String configLocation;
    private String typeAliasesPackage;

    public String getMapperLocations() {
        return "classpath:" + mapperLocations;
    }

    public void setMapperLocations(String mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public String getConfigLocation() {
        return "classpath:" + configLocation;
    }

    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

    public String getTypeAliasesPackage() {
        return typeAliasesPackage;
    }

    public void setTypeAliasesPackage(String typeAliasesPackage) {
        this.typeAliasesPackage = typeAliasesPackage;
    }

}
