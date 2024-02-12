package com.study.connection.dto;

import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

/**
 * error 페이지를 따로 만드는 대신 , redirectUri 를 넘겨 이동한다.
 */
@Builder
@Getter
public class ErrorDto {
    @Nullable private String redirectUri;
    private String errorMessage;
    private Integer status;
}

