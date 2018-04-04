package com.lebron.springboot.config.druid;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.read.one")
public class ReadOneDataSourceBuilder extends DataSourceBuilder {

}
