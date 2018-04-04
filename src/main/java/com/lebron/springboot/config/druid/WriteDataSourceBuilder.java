package com.lebron.springboot.config.druid;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.write.datasource")
public class WriteDataSourceBuilder extends DataSourceBuilder {

}
