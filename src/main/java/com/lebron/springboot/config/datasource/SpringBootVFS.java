package com.lebron.springboot.config.datasource;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.io.VFS;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

//Spring Boot集成MyBatis打包成jar时，找不到类的问题
//MyBatis扫描通过VFS来实现
//在Spring Boot中，由于是嵌套Jar，导致Mybatis默认的VFS实现DefaultVFS无法扫描嵌套Jar中的类。
//解决办法，实现自定义的VFS，参考DefaultVFS增加对Spring Boot嵌套JAR的处理。
public class SpringBootVFS extends VFS {

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    protected List<String> list(URL url, String path) throws IOException {
        ClassLoader cl = this.getClass().getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
        Resource[] resources = resolver.getResources(path + "/**/*.class");
        List<Resource> resources1 = Arrays.asList(resources);
        List<String> resourcePaths = new ArrayList<String>();
        for (Resource resource : resources1) {
            resourcePaths.add(preserveSubpackageName(resource.getURI(), path));
        }
        return resourcePaths;
    }

    private static String preserveSubpackageName(final URI uri, final String rootPath) {
        final String uriStr = uri.toString();
        final int start = uriStr.indexOf(rootPath);
        return uriStr.substring(start, uriStr.length());
    }

}
