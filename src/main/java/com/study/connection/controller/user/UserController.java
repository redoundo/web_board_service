package com.study.connection.controller.user;

import com.study.connection.auth.JwtProvider;
import com.study.connection.dto.JwtDto;
import com.study.connection.dto.UserDto;
import com.study.connection.entity.UserEntity;
import com.study.connection.error.CustomRuntimeException;
import com.study.connection.error.ErrorCode;
import com.study.connection.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import static com.study.connection.utils.CheckValid.checking;

/**
 * 로그인, 회원가입 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"/api"})
public class UserController {
    private final JwtProvider jwtProvider;
    private final AuthService authService;

    /**
     * 받은 내용을 통해 사용자를 생성하고 jwt 를 반환한다.
     * @param user 생성할 사용자 정보
     * @return jwt 토큰
     */
    @PostMapping(value = {"/auth/signIn"})
    public ResponseEntity<JwtDto> signIn( @RequestBody UserDto user){

        JwtDto jwt = this.authService.signIn(user);
        return ResponseEntity.ok(jwt);
    }

    /**
     * id, password 가 존재하고 일치하는 사용자가 존재한다면 jwt 토큰을 생성해 반환한다.
     * @param loginArg id, password
     * @return jwt 토큰
     */
    @PostMapping(value = {"/auth/login"})
    public ResponseEntity<JwtDto> login( @RequestBody Map<String, String> loginArg){

        String id = loginArg.get("id");
        String password = loginArg.get("password");

        if(!checking.checkString(id) || !checking.checkString(password)){
            throw new CustomRuntimeException(ErrorCode.MISSED_AUTHORITY_ARGUMENT);
        }

        JwtDto jwt = this.authService.login(id, password);
        Authentication authentication = jwtProvider.getAuthentication(jwt.getAccessToken());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok(jwt);
    }

    /**
     * 사용자가 입력한 아이디가 사용 가능한지 확인하고 결과를 반환.
     * @param id 사용자가 입력한 아이디
     * @return 아이디 사용 가능 여부
     */
    @PostMapping(value = {"/auth/signIn/checkId"})
    public ResponseEntity<Boolean> canUseThisId( @RequestBody Map<String, String> id){

        if(!checking.checkString(id.get("id"))) return ResponseEntity.ok(false);
        Boolean canUse = this.authService.canUseThisId(id.get("id"));

        return ResponseEntity.ok(canUse);
    }

    /**
     * 토큰에서 사용자 정보를 가져온 뒤, 그 내용으로 정보를 가져온다.
     * @param userDetails 헤더에 설정된 jwt 토큰
     * @return 사용자 정보.
     */
    @GetMapping(value = {"/user/main", "/user/validate"})
    public ResponseEntity<UserEntity> findUserInfoByJwtToken( @AuthenticationPrincipal UserDetails userDetails){

        if(userDetails == null) throw new CustomRuntimeException(ErrorCode.NEED_LOGIN_EXCEPTION);
        String userStringId = userDetails.getUsername();
        if(!checking.checkString(userStringId)) throw new CustomRuntimeException(ErrorCode.ILLEGAL_JWT_EXCEPTION);
        UserEntity user = this.authService.findUser((int) Float.parseFloat(userStringId));
        if(user == null) throw new CustomRuntimeException(ErrorCode.NEED_SIGN_IN_EXCEPTION); // 로그인은 되어있지만 사용자 정보가 존재하지 않을 때.

        return ResponseEntity.ok(user);
    }
}
