package com.study.connection.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 애러 코드
 */
@Getter
public enum ErrorCode {

    SELECT_FAILED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR , -100 , "게시물들을 가져오는데 실패했습니다") ,
    DELETE_FAILED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR , -101 , "게시물을 삭제하는데 실패했습니다") ,
    UPDATE_FAILED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR , -102 , "게시물을 수정하는데 실패했습니다") ,
    INSERT_FAILED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR , -103 , "게시물을 생성하는데 실패했습니다") ,

    /**
     * update , insert 에 반드시 필요한 내용 중 일부가 존재하지 않을 때.
     */
    MISSING_CONTENT_ERROR(HttpStatus.BAD_REQUEST , -200 , "필요한 내용이 충족되지 않았습니다") ,
    /**
     * 데이터베이스의 비밀번호와 사용자가 입력한 비밀번호가 동일하지 않을 때.
     */
    MISMATCH_PASSWORD_ERROR(HttpStatus.FORBIDDEN , -201 , "비밀번호가 일치하지 않습니다") ,
    /**
     * 존재하지 않는 파일을 다운로드 하려고 할때.
     */
    CANT_FIND_FILE_ERROR(HttpStatus.NOT_FOUND , -300 , "해당 파일이 존재하지 않습니다"),
    /**
     *
     * resource.contentLength() 에서 resource 를 닫지 못해 생기는 예외이다. 대처 방안이 없으므로 일단 예외로 등록한다.
     */
    IOEXCEPTION_WHILE_DOWNLOAD_FILE(HttpStatus.INTERNAL_SERVER_ERROR , -301 , "파일을 다운로드 하는 중에 문제가 발생했습니다."),
    /**
     * 존재하지 않는 contentId 를 가진 url 로 진입시.
     */
    INVALID_CONTENT_ID_ERROR(HttpStatus.NOT_FOUND  , -202, "해당 게시물이 존재하지 않습니다") ,
    /**
     * 문자열의 유효성에 문제가 있을 경우 발생. null , blank , empty
     */
    INVALID_STRING_ARGUMENT(HttpStatus.BAD_REQUEST , -203 , "문자열이 null 이거나 , 비어있거나 혹은 값이 없습니다"),

    FILE_NOT_DELETED(HttpStatus.INTERNAL_SERVER_ERROR , -302 , " 파일을 삭제하려고 시도하려고 했으나 , 파일이 삭제되지 않았습니다. 다시 시도하세요."),

    ON_ENCRYPT_NO_ALGORITHM(HttpStatus.INTERNAL_SERVER_ERROR  , -204 , "현재 서버 환경에서 암호화가 불가능합니다. 관리자에게 문의하세요."),
    ;

    private final HttpStatus status;
    private final Integer codeStatus;
    private final String statusMessage;

    ErrorCode(HttpStatus status, Integer codeStatus , String statusMessage ){
        this.status = status;
        this.codeStatus = codeStatus;
        this.statusMessage = statusMessage;
    }
}
