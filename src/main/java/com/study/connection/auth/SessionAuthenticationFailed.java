package com.study.connection.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * session 인증에 실패하면 해당 session 에 errorMessage 저장한 뒤, 로그인 페이지로 redirect 시킨다.
 * 로그인 페이지는 errorMessage 가 존재하면 alert 로 인증에 왜 실패했는지 알려준다.
 */
@Component
@RequiredArgsConstructor
public class SessionAuthenticationFailed implements AuthenticationFailureHandler {
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = exception.getMessage();
        request.getSession().setAttribute("errorMessage", errorMessage);
        this.redirectStrategy.sendRedirect(request, response, "/auth/login");
    }
}
