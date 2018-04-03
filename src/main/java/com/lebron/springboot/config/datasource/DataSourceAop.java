package com.lebron.springboot.config.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DataSourceAop {

	public static final Logger logger = LoggerFactory.getLogger(DataSourceAop.class);

	@Value("${readSuffix}")
	private String readSuffix;

	@Before("execution(* com.lebron.springboot.service..*.*(..))")
	public void switchDataSourceType(JoinPoint joinPoint) {
		String[] readSuffixArr = readSuffix.split(",");
		for (String read : readSuffixArr) {
			String name = joinPoint.getSignature().getName().toLowerCase();
			if (name.startsWith(read)) {
				logger.info("dataSource切换到：Read");
				DataSourceContextHolder.read();
				return;
			}
		}
		logger.info("dataSource切换到：write");
		DataSourceContextHolder.write();
	}

}