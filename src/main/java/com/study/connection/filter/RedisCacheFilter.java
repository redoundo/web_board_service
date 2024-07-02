package com.study.connection.filter;

import com.study.connection.service.CacheService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * SecurityContextRepository 에 존재하지 않으며 HttpSession 이 실행 되지도 않은 경우 실행한다.
 * 즉, 로그인을 한 뒤에 별도의 호출이 없으면 aws lambda 가 꺼지게 되는데,
 * sessionId 가 있고 redis 에 저장된 authentication 이 expire 되지 않았을 경우,
 * 저장된 authentication 을 SecurityContextRepository 에 다시 저장 시킨다.
 */
@RequiredArgsConstructor
public class RedisCacheFilter  extends OncePerRequestFilter {
    private final SecurityContextRepository repository ;
    private final CacheService cacheService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(!repository.containsContext(request)){
            Cookie[] cookies = request.getCookies();
            Cookie redisCookie = null;
            for(Cookie cookie : cookies) {
                if (cookie.getName() != null && cookie.getName().equals("redisKey")){
                    redisCookie = cookie;
                }
            }

            // header 에 sessionId 가 존재할 경우
            if (redisCookie != null) {
                String redisValue = redisCookie.getValue();
                // redis 에서 sessionId 로 authentication 을 가져온다.
                Authentication authentication = cacheService.parseACache(redisValue);
                // 만약 expired 되거나 아예 존재하지 않을 경우는 제외하고 진행한다.
                if(authentication != null){
                    // context 를 새롭게 생성한 뒤 가져온 authentication 을 넣어 로그인 상태를 유지시킨다.
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(authentication);
                    repository.saveContext(context, request, response);
                    request.setAttribute("redisKey", redisValue);
                    response.setHeader("redisKey", redisValue);
                    Cookie cookie = new Cookie("redisKey", redisValue);
                    cookie.setPath("/");
                    cookie.setMaxAge(12000);
                    cookie.setAttribute("redisKey", redisValue);
                    response.addCookie(cookie);
                }
            }
        }
        else {
            String sessionId = request.getRequestedSessionId();
            Authentication authentication = cacheService.parseACache(sessionId);
            if(authentication != null){
                request.setAttribute("redisKey", sessionId);
                response.setHeader("redisKey", sessionId);
                Cookie cookie = new Cookie("redisKey", sessionId);
                cookie.setMaxAge(12000);
                cookie.setPath("/");
                cookie.setAttribute("redisKey", request.getRequestedSessionId());
                response.addCookie(cookie);
            }
        }
        filterChain.doFilter(request, response);
    }
}
