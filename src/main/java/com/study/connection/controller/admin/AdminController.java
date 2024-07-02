package com.study.connection.controller.admin;

import com.study.connection.auth.AuthService;
import com.study.connection.auth.UserDetailServiceImpl;
import com.study.connection.entity.UserEntity;
import com.study.connection.error.ErrorCode;
import com.study.connection.error.MvcRuntimeException;
import com.study.connection.service.CacheService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import static com.study.connection.utils.CheckValid.checking;

/**
 * 관리자 인증 컨트롤러
 */
@Controller
@RequiredArgsConstructor
public class AdminController {
    private final SecurityContextRepository repository = new HttpSessionSecurityContextRepository();
    private final SessionRegistry registry;
    private final AuthService authService;
    private final UserDetailServiceImpl impl;
    private final CacheService cacheService;

    /**
     * 로그인을 진행하기 전에 sessionId 가 있는지 확인을 거친다. 
     * @param request session 확인이 필요한 request
     * @param model 내용 저장을 위한 model
     * @return 렌더링할 뷰 위치
     */
    @GetMapping(value = {"/login", "/main", "/auth/login"})
    public String LoginPage(HttpServletRequest request, Model model){

        if(!repository.containsContext(request)){
            return "auth/login";  // 세션에 저장된 sessionId 가 없으면 바로 login page 로 이동
        }
        SecurityContext context = repository.loadDeferredContext(request).get();
        Authentication authentication = context.getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if(userDetails == null || userDetails.getUsername() == null) {
            request.setAttribute("redisKey", null);
            return "auth/login";
        }

        UserEntity userEntity = this.authService.findUser((int) Float.parseFloat(userDetails.getUsername()));
        if (userEntity == null || !userEntity.getRole().equals("ROLE_ADMIN")) {
            request.setAttribute("redisKey", null);
            throw new MvcRuntimeException(
                    ErrorCode.UNAUTHORIZED_ACCESS_EXCEPTION, "auth/login", "http://localhost:5000/main", model);
        }
        else {
            request.setAttribute("redisKey", request.getRequestedSessionId());
            return "redirect:/admin/main";
        }
    }

    /**
     * id 와 password 를 request 에서 찾아 존재하는 회원인지, 회원이 관리자 권한을 갖고 있는지 확인한다.
     * 성공할 경우 sessionId 를 SecurityContext 에 저장시킨다.
     * @param request id 와 password 가 존재해야하는 request
     * @param response sessionId 저장을 위해 필요한 response
     * @param model 내용을 저장할 model
     * @return 이동할 위치
     */
    @PostMapping(value = {"/auth/login/process"})
    public String LoginProcess(HttpServletRequest request, HttpServletResponse response, Model model){

        String id = request.getParameter("id");
        String password = request.getParameter("password");
        if(!checking.checkString(id) && !checking.checkString(password))
            throw new MvcRuntimeException(
                    ErrorCode.MISSING_CONTENT_ERROR, "auth/login", "/auth/login", model);

        // 대부분 /auth/login 에서 redirect 되었겠지만, 그래도 세션에 저장된 정보가 존재하는지, 존재한다면 유효한지 확인
        if(repository.containsContext(request)) {
            SessionInformation info = registry.getSessionInformation(request.getRequestedSessionId());
            if(info != null && !info.isExpired()){ // 발급받은 sessionId 가 존재 한다면 진행.
                Authentication auth = repository.loadDeferredContext(request).get().getAuthentication();
                UserDetails userDetails = (UserDetails) auth.getPrincipal();
                UserEntity userEntity = this.authService.findUser((int) Float.parseFloat(userDetails.getUsername()));

                if (userEntity == null || !userEntity.getRole().equals("ROLE_ADMIN"))
                    throw new MvcRuntimeException(
                            ErrorCode.UNAUTHORIZED_ACCESS_EXCEPTION, "auth/login", "http://localhost:5000/main", model);
                else return "redirect:/admin/main";
            } // security context 에 사용자 정보가 존재하지 않으면 아래 과정 진행.
        } // 세션이 만료 되었거나 세션 생성이 되지 않았을 때만 아래의 과정 진행.

        // this.impl.loadUserByUsername 에서 userEntity 를 가져올 떄 role == role_admin 이 아니면 UNAUTHORIZED_ACCESS_EXCEPTION 이 던져진다.
        UserDetails details = this.impl.loadUserByUsername(id);
        Authentication auth = new UsernamePasswordAuthenticationToken(details, "", details.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        this.repository.saveContext(context, request, response);
        String sessionId = request.getRequestedSessionId();
//        this.registry.registerNewSession(sessionId, details);
        this.cacheService.putCache(sessionId, new UsernamePasswordAuthenticationToken(details, "", details.getAuthorities()));
        Cookie cookie = new Cookie("redisKey", request.getRequestedSessionId());
        cookie.setMaxAge(12000);
        cookie.setPath("/");
        cookie.setAttribute("redisKey", sessionId);
        response.addCookie(cookie);
        request.setAttribute("redisKey", sessionId);
        response.setHeader("redisKey", sessionId);
        return "redirect:/admin/main";
    }

}
