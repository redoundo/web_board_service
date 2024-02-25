package com.study.connection.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * error code 를 담는 클래스
 */
@Getter
@AllArgsConstructor
public class CustomRuntimeException extends RuntimeException{
    ErrorCode errorCode;
}
