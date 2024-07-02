package com.study.connection.service;

import com.study.connection.dto.RedisDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

/**
 * 캐시 crud 서비스
 */
@Service
@RequiredArgsConstructor
public class CacheService {
    private final RedisDao redisDao;

    /**
     * redis 의 내용을 캐싱 해온다.
     * @param sessionId request 헤더에 존재하는 sessionId
     * @return 관리자 정보
     */
    public Authentication parseACache(String sessionId){
        return redisDao.getValues(sessionId);
    }

    /**
     * 관리자가 로그인 뒤 바로 저장한다.
     * key: sessionId, 관리자 정보: UsernamePasswordAuthenticationToken
     * @param sessionId key
     * @param authenticationToken value
     */
    @Transactional
    public void putCache(String sessionId, UsernamePasswordAuthenticationToken authenticationToken){
        redisDao.setValues(sessionId, authenticationToken, Duration.ofMinutes(20));
    }

    /**
     * sessionId 로 관리자 정보 삭제.
     * @param sessionId key
     */
    @Transactional
    public void deleteCache(String sessionId) {
        redisDao.deleteValues(sessionId);
    }


}
