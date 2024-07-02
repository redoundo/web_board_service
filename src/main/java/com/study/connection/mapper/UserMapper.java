package com.study.connection.mapper;

import com.study.connection.dto.UserDto;
import com.study.connection.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * users 테이블의 mapper
 */
@Mapper
@Repository
public interface UserMapper {
    /**
     * 사용자가 입력한 아이디와 비번으로 사용자 정보를 찾아 반환한다. 없다면 null 을 반환.
     * @param id 사용자가 입력한 아이디
     * @param password 사용자가 입력한 비밀번호
     * @return 사용자 정보 || null
     */
    UserEntity findUserByIdPassword(@Param("id") String id, @Param("password") String password);
    /**
     * userId를 받아 사용자 정보를 반환한다. userId로 찾는 것이므로 이미 존재한다고 가정하고 진행한다.
     * @param id userId
     * @return 사용자 정보
     */
    UserEntity findUserByUserId(Integer id);

    /**
     * 아이디로 사용자 찾기.
     * @param id 사용자가 입력한 아이디
     * @return 사용자의 아이디
     */
    Integer findUserIdById(String id);
    /**
     * 사용자 이름으로 사용자 정보를 가져온다. 
     * @param nickname 사용자 이름
     * @return 사용자 정보
     */
    UserEntity findUserByNickname(String nickname);

    /**
     * 회원가입을 할때 해당 아이디를 이용할 수 있는지 확인한다.
     * @param id 사용자 아이디
     * @return 사용 가능 여부
     */
    Boolean canUseThisId(String id);

    /**
     *
     * @param id 입력한 아이디
     * @return 해당 아이디의 사용자 존재 여부
     */
    Boolean isUserExist(String id);

    /**
     * 사용자를 생성한다.
     * @param user 저장할 사용자 정보.
     */
    void newUser(UserDto user);

    /**
     * 사용자 삭제
     * @param id userId
     */
    void deleteUser(Integer id);
}
