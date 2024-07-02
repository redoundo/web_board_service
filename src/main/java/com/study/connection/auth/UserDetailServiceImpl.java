package com.study.connection.auth;

import com.study.connection.entity.UserEntity;
import com.study.connection.error.CustomRuntimeException;
import com.study.connection.error.ErrorCode;
import com.study.connection.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Integer userId = userMapper.findUserIdById(id);

        UserEntity userEntity = userMapper.findUserByUserId(userId);

        if(userEntity == null) throw new CustomRuntimeException(ErrorCode.NEED_SIGN_IN_EXCEPTION);
        if(!userEntity.getRole().equals("ROLE_ADMIN"))
            throw new CustomRuntimeException(ErrorCode.UNAUTHORIZED_ACCESS_EXCEPTION);

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userEntity.getRole());
        List<GrantedAuthority> lists = new ArrayList<>();
        lists.add(authority);

        return new User(userEntity.getUserId().toString(), userEntity.getPassword(), lists);
    }

    /**
     * session 에 저장되어 있던 userDetail 로 userId 를 가져온다.
     * 승인된 권한이 ROLE_ADMIN 일 경우에만 userId 가 반환된다.
     * @return userId || exception
     */
    public Integer findUserAuthentication(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated())
            throw new CustomRuntimeException(ErrorCode.FAILED_AUTHORIZED_EXCEPTION);

        UserDetails details = (UserDetails) authentication.getPrincipal();
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("ADMIN"));
        list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if(details.getAuthorities() == null ||
                details.getAuthorities().isEmpty() ||
                list.contains(details.getAuthorities().stream().toList().get(0)))
            throw new CustomRuntimeException(ErrorCode.FAILED_AUTHORIZED_EXCEPTION);

        Integer userId = this.userMapper.findUserIdById(details.getUsername());
        if(userId == null) throw new CustomRuntimeException(ErrorCode.NEED_SIGN_IN_EXCEPTION);

        return userId;
    }
}
