package com.study.connection.auth;

import com.study.connection.dto.JwtDto;
import com.study.connection.dto.UserDto;
import com.study.connection.entity.UserEntity;
import com.study.connection.error.CustomRuntimeException;
import com.study.connection.error.ErrorCode;
import com.study.connection.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static com.study.connection.utils.CheckValid.checking;

/**
 * 회원 가입, 로그인 관련 서비스.
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;
    /**
     * 사용자가 입력한 비밀번호와 아이디를 통해 jwt 토큰을 발행시킨다.
     * @param id 입력한 아이디
     * @param password 사용자가 입력한 비밀번호
     * @return jwt 토큰 || 예외 발생.
     */
    public JwtDto login(String id, String password){
        if(!checking.checkString(id) && !checking.checkString(password)){
            throw new CustomRuntimeException(ErrorCode.FAILED_AUTHORIZED_EXCEPTION);
        }

        Boolean isExist = this.userMapper.isUserExist(id);
        if(!isExist) {
            throw new CustomRuntimeException(ErrorCode.NEED_SIGN_IN_EXCEPTION);
        }

        UserEntity userInfo = this.userMapper.findUserByIdPassword(id, password);
        if(userInfo == null) {
            throw new CustomRuntimeException(ErrorCode.NEED_SIGN_IN_EXCEPTION);
        }

        return jwtProvider.returnJwt(userInfo);
    }

    public UserEntity validateJwt(String token){
        if(!checking.checkString(token)) return null;
        if(!this.jwtProvider.isValid(token)) return null;
        Integer userId = this.jwtProvider.parseUserId(token);
        if(userId == null) return null;
        return this.userMapper.findUserByUserId(userId);
    }

    /**
     * 사용자가 입력한 회원가입 내용을 저장하고 회원정보를 받아와 jwt 토큰을 발행시킨다.
     * @param user 입력한 내용.
     * @return jwt 토큰
     */
    public JwtDto signIn(UserDto user){
        this.userMapper.newUser(user);
        UserEntity userInfo = this.userMapper.findUserByIdPassword(user.getId(), user.getPassword());
        if(userInfo == null){
            this.userMapper.newUser(user); // 다시 집어넣는다.
            UserEntity info= this.userMapper.findUserByIdPassword(user.getId(), user.getPassword());
            return jwtProvider.returnJwt(info);
        }
        return jwtProvider.returnJwt(userInfo);
    }

    /**
     * 아이디 사용 가능한지 확인.
     * @param id 입력한 아이디
     * @return 사용 가능 여부
     */
    public Boolean canUseThisId(String id){
        return this.userMapper.canUseThisId(id);
    }

    /**
     * 사용자 아이디로 정보를 반환.
     * @param userId 사용자 아이디
     * @return 사용자 정보
     */
    public String findUserByUserId(Integer userId){
        return this.userMapper.findUserByUserId(userId).getNickname();
    }

    /**
     * 사용자 아이디로 사용자 정보 반환.
     * @param userId 사용자 아이디
     * @return userEntity
     */
    public UserEntity findUser(Integer userId){
        return this.userMapper.findUserByUserId(userId);
    }

}
