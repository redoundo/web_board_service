package com.study.connection.entity;

import lombok.*;

/**
 * auto_increase 한 userId, role 포함.
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    private Integer userId;
    private String id;
    private String password;
    private String nickname;
    private String role;
}
