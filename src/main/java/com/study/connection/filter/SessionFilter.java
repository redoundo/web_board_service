package com.study.connection.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * 관리자 인증시 진행되는 security filter chain
 */
@RequiredArgsConstructor
public class SessionFilter extends OncePerRequestFilter {
    private final SessionRegistry sessionRegistry;
    private final SecurityContextRepository repository;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        // security context 에 등록된 sessionId 중 request 에 들어있는 sessionId 가 없으면 진행.
        if(!repository.containsContext(request)){
            // session 이 생성되어 있지 않거나 session 에 아예 정보가 없으면 진행.
            if (session != null && session.getId() != null) {
                String sessionId = session.getId();
                SessionInformation info = sessionRegistry.getSessionInformation(sessionId);
                // 정보가 유효하고 security context 에 등록되어 있지 않는 경우에만 추가적으로 등록
                if (info != null && !info.isExpired()) {
                    UserDetails details = (UserDetails) info.getPrincipal();
                    Authentication auth =
                            new UsernamePasswordAuthenticationToken(details, "", details.getAuthorities());
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(auth);
                    SecurityContextHolder.setContext(context);
                    repository.saveContext(context, request, response);
                }
            } else request.getSession(true); // session 이 없거나 session 정보가 없으면 생성
        } else {
            if (session != null && session.getId() != null) request.getSession(true);
        }
        // 위의 과정을 진행하지 않은 경우 controller 에서 직접 등록한다.
        filterChain.doFilter(request, response);
    }
}
