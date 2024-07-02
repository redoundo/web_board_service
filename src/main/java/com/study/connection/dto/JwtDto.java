package com.study.connection.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class JwtDto {
    private String accessToken;
    private String refreshToken;
    private String grantType;
    private Date accessTokenExpiredAt;
}
