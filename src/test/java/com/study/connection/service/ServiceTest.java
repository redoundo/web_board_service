package com.study.connection.service;

import com.study.connection.auth.AuthSerializer;
import com.study.connection.dto.RedisDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("service 계층 테스트")
public class ServiceTest {
    private CacheService cacheService;

    @BeforeEach
    void setUp() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setPassword("****"); // 개인 정보를 위해 삭제
        config.setPort(11111);
        config.setHostName("***********");
        config.setUsername("default");
        LettuceConnectionFactory factory = new LettuceConnectionFactory(config);
        factory.start();
        RedisTemplate<String, UsernamePasswordAuthenticationToken> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new AuthSerializer( ));
        template.setEnableTransactionSupport(true);
        template.setConnectionFactory(factory);
        template.afterPropertiesSet();

        RedisDao dao = new RedisDao(template);

        this.cacheService = new CacheService(dao);
    }
    
    @Test
    @DisplayName("redis 캐싱 테스트")
    void redisCacheTest() throws InterruptedException {
        String sessionId = "wQasdd5P5331eUIw24R24rbfk45J141";
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        List<GrantedAuthority> lists = new ArrayList<>();
        lists.add(authority);
        UserDetails details = new User("1", "rOlE329019", lists);
        this.cacheService.putCache(sessionId, new UsernamePasswordAuthenticationToken(details, "", details.getAuthorities()));

        Thread.sleep(2000);
        Authentication authentication = this.cacheService.parseACache(sessionId);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<GrantedAuthority> roles = (List<GrantedAuthority>) authentication.getAuthorities();
        assertEquals("1", userDetails.getUsername());
        assertEquals("rOlE329019", userDetails.getPassword());
        assertEquals("ROLE_ADMIN", roles.getFirst().getAuthority());

        this.cacheService.deleteCache(sessionId);
    }
}
