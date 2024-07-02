package com.study.connection.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

/**
 * resource handler
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig  implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/**")
                .addResourceLocations("classpath:/static/" , "classpath:/template/")
                .setCacheControl(CacheControl.maxAge(10 , TimeUnit.MINUTES))
                .resourceChain(true)
        ;
        registry.addResourceHandler("/admin/gallery/**" ,"/admin/gallery/update/**", "/admin/gallery/write/insert/**")
                .addResourceLocations("C:/Users/admin/file/**")
                .setCacheControl(CacheControl.maxAge(10 , TimeUnit.MINUTES))
                .resourceChain(true);
    }

}