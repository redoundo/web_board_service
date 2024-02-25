package com.study.connection.dto.content;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * content 내용 변경시 변경 가능한 내용만 제공.
 */
@Builder
@Getter
@Setter
@Validated
public class UpdateContentDto {

    @NotNull(message = "게시글 수정을 위해서 작성자는 필수 내용입니다.")
    @NotEmpty(message = "게시글 수정을 하려면 작성자는 반드시 필요합니다.")
    @NotBlank(message = "빈칸은 유효한 작성자 이름이 아닙니다.")
    private String nickname;

    @NotNull(message = "게시글 제목 수정을 하려면 수정할 제목 내용이 필요합니다.")
    @NotBlank(message = "빈칸은 유효한 게시글 제목이 아닙니다.")
    @NotEmpty(message = "게시글 제목 수정을 하려면 수정할 제목 내용이 필요합니다.")
    private String title;

    @NotNull(message = "게시글을 수정하기 위해서 사용자 인증이 필요합니다.")
    @NotEmpty(message = "게시글을 수정하기 위해서 사용자 인증이 필요합니다.")
    @NotBlank(message = "빈칸은 유효한 비밀번호가 아닙니다.")
    private String password;

    @NotNull(message = "게시글의 카테고리를 수정하기 위해서는 카테고리 아이디가 필요합니다.")
    @Min(1)
    private Integer contentCategoryId;
}
