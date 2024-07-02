package com.study.connection.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.List;

/**
 * redis db set get delete
 */
@Component
@RequiredArgsConstructor
public class RedisDao {
    private final RedisTemplate<String, UsernamePasswordAuthenticationToken> redisTemplate;

    public void setValues(String key, UsernamePasswordAuthenticationToken authenticationToken) {
        ValueOperations<String, UsernamePasswordAuthenticationToken> values = redisTemplate.opsForValue();
        values.set(key, authenticationToken);
    }

    public void setValuesList(String key, List<UsernamePasswordAuthenticationToken> authenticationTokenList) {
        redisTemplate.opsForList().rightPushAll(key, authenticationTokenList);
    }

    public void setValues(String key, UsernamePasswordAuthenticationToken authenticationToken, Duration duration) {
        ValueOperations<String, UsernamePasswordAuthenticationToken> values = redisTemplate.opsForValue();
        values.set(key, authenticationToken, duration);
    }

    public UsernamePasswordAuthenticationToken getValues(String key) {
        ValueOperations<String, UsernamePasswordAuthenticationToken> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }

}
