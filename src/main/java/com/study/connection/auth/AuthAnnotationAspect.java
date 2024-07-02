package com.study.connection.auth;


import com.study.connection.error.CustomRuntimeException;
import com.study.connection.error.ErrorCode;
import com.study.connection.utils.CheckValid;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import static com.study.connection.utils.CheckValid.checking;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthAnnotationAspect {

    private final HttpSession session;

    @Around("execution(* com.study.connection.controller.user.PrivateAccessController..*(.., @com.study.connection.auth.AuthAnnotation (*), ..))")
    public Object userIdInAuthentication(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) throw new CustomRuntimeException(ErrorCode.NEED_LOGIN_EXCEPTION);

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (userDetails == null || !checking.checkString(userDetails.getUsername()))
            throw new CustomRuntimeException(ErrorCode.FAILED_AUTHORIZED_EXCEPTION);

        Integer userId = (int) Float.parseFloat(userDetails.getUsername());
        Object[] objects = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        for (int i = 0; i < methodSignature.getMethod().getParameterCount(); i++) {
            if (methodSignature.getMethod().getParameters()[i].isAnnotationPresent(AuthAnnotation.class)) {
                objects[i] = userId;
            }
        }
        return joinPoint.proceed(objects);
    }

    @Around("execution(* com.study.connection.controller.admin.PrivateRestController..*(.., @com.study.connection.auth.AuthAnnotation (*), ..))")
    public Object adminIdRestAuthentication(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) throw new CustomRuntimeException(ErrorCode.NEED_LOGIN_EXCEPTION);

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if(userDetails == null || userDetails.getUsername() == null || this.session.getId() == null)
            throw new CustomRuntimeException(ErrorCode.FAILED_AUTHORIZED_EXCEPTION);

        if(!CheckValid.checking.checkGrantedAuthority(userDetails))
            throw new CustomRuntimeException(ErrorCode.UNAUTHORIZED_ACCESS_EXCEPTION);

        Integer userId = (int) Float.parseFloat(userDetails.getUsername());
        Object[] objects = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        for (int i = 0; i < methodSignature.getMethod().getParameterCount(); i++) {
            if (methodSignature.getMethod().getParameters()[i].isAnnotationPresent(AuthAnnotation.class)) {
                objects[i] = userId;
            }
        }
        return joinPoint.proceed(objects);
    }
}

