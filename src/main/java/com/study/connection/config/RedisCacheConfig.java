package com.study.connection.config;
import com.study.connection.auth.AuthSerializer;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * redis configs
 */
@EnableCaching
@Configuration
@EnableRedisRepositories
@PropertySource("classpath:application.properties")
@ImportAutoConfiguration({RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class})
public class RedisCacheConfig implements CachingConfigurer {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password}")
    private String password;

    @Value("${spring.data.redis.username}")
    private String userName;

    /**
     * redis 연결
     * @return redisConnectionFactory
     */
    @Bean("redisConnectionFactory")
    public RedisConnectionFactory redisConnectionFactory() {

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setPassword(password);
        config.setPort(port);
        config.setHostName(host);
        config.setUsername(userName);

        return new LettuceConnectionFactory(config);
    }

    /**
     * 객체와 데이터 직렬화 역직렬화 진행
     * @return RedisTemplate
     */
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, UsernamePasswordAuthenticationToken> redisTemplate() {
        RedisTemplate<String, UsernamePasswordAuthenticationToken> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new AuthSerializer( ));
        template.setConnectionFactory(redisConnectionFactory());
        template.setEnableTransactionSupport(true);
        return template;
    }
}
