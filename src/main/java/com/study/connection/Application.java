package com.study.connection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.cache.CachesEndpointAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableCaching
@SpringBootApplication
@EnableAspectJAutoProxy
@ImportAutoConfiguration({ErrorMvcAutoConfiguration.class, CachesEndpointAutoConfiguration.class,
        WebMvcAutoConfiguration.class, AopAutoConfiguration.class, CacheAutoConfiguration.class,
        DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class,
        DispatcherServletAutoConfiguration.class, EmbeddedWebServerFactoryCustomizerAutoConfiguration.class, HttpEncodingAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class, JacksonAutoConfiguration.class, WebSocketServletAutoConfiguration.class, })
public class Application {
    public static void main(String[] args){
        long startTime = System.currentTimeMillis();
        SpringApplication.run(Application.class, args);
        long endTime = System.currentTimeMillis();
        long initTime = endTime - startTime;
        System.out.println("Spring Boot Initialize Time:    " +  initTime);
    }
}


