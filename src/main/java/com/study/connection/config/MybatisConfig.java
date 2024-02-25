package com.study.connection.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.study.connection.mapper"})
public class MybatisConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String driverName;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String userName;
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName(driverName)
                .password(password)
                .url(url)
                .username(userName)
                .build();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory () throws Exception{
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource());
        bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("/mybatis-config.xml"));
        bean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResource("/mapper/CategoryMapper.xml")
                ,new PathMatchingResourcePatternResolver().getResource("/mapper/ContentMapper.xml")
                ,new PathMatchingResourcePatternResolver().getResource("/mapper/FileMapper.xml")
                ,new PathMatchingResourcePatternResolver().getResource("/mapper/CommentMapper.xml")
        );
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory factory){
        return new SqlSessionTemplate(factory);
    }
}

