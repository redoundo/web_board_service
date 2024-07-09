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

/**
 * 사용자 / 관리자 인증, 에러 처리
 */
@Aspect
@Component
@RequiredArgsConstructor
public class AuthAnnotationAspect {

    private final HttpSession session;

    /**
     * 사용자 페이지에 필요한 Authentication 처리
     * @param joinPoint 어드바이스 적용 지점 내용
     * @return 다음 어드바이스나 타겟 호출
     * @throws Throwable 에러
     */
    @Around("execution(* com.study.connection.controller.user.PrivateAccessController..*(.., @com.study.connection.auth.AuthAnnotation (*), ..))")
    public Object userIdInAuthentication(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) throw new CustomRuntimeException(ErrorCode.NEED_LOGIN_EXCEPTION);

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (userDetails == null || !checking.checkString(userDetails.getUsername()))
            throw new CustomRuntimeException(ErrorCode.FAILED_AUTHORIZED_EXCEPTION);

        Object[] objects = this.duplicateRaw(userDetails, joinPoint);
        return joinPoint.proceed(objects);
    }

    /**
     * 관리자 페이지에서 rest api 로 요청시 사용된다.
     * @param joinPoint 어드바이스 적용 지점 내용
     * @return  다음 어드바이스나 타겟 호출
     * @throws Throwable 에러
     */
    @Around("execution(* com.study.connection.controller.admin.PrivateRestController..*(.., @com.study.connection.auth.AuthAnnotation (*), ..))")
    public Object adminIdRestAuthentication(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) throw new CustomRuntimeException(ErrorCode.NEED_LOGIN_EXCEPTION);

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if(userDetails == null || userDetails.getUsername() == null || this.session.getId() == null)
            throw new CustomRuntimeException(ErrorCode.FAILED_AUTHORIZED_EXCEPTION);

        if(!CheckValid.checking.checkGrantedAuthority(userDetails))
            throw new CustomRuntimeException(ErrorCode.UNAUTHORIZED_ACCESS_EXCEPTION);

        Object[] objects = this.duplicateRaw(userDetails, joinPoint);
        return joinPoint.proceed(objects);
    }

    /**
     * AuthAnnotation.class 와 같은 타입의 매개변수를 가지고 와서 값을 바꿔준다.
     * @param userDetails 사용자 정보
     * @param joinPoint 어드바이스 적용 지점 내용
     * @return 변경한 어드바이스 적용 지점 내용
     */
    public Object[] duplicateRaw(UserDetails userDetails, ProceedingJoinPoint joinPoint) {
        int userId = (int) Float.parseFloat(userDetails.getUsername());
        Object[] objects = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        for (int i = 0; i < methodSignature.getMethod().getParameterCount(); i++) {
            if (methodSignature.getMethod().getParameters()[i].isAnnotationPresent(AuthAnnotation.class)) {
                objects[i] = userId;
            }
        }
        return objects;
    }
}

