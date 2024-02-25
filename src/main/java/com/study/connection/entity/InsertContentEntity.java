package com.study.connection.entity;

import jakarta.validation.constraints.*;
import lombok.*;
import org.jetbrains.annotations.Nullable;
import org.springframework.validation.annotation.Validated;

/**
 * insert 시 사용되는 entity
 */
@Getter
@Setter
@Builder
@Validated
public class InsertContentEntity {
    @Nullable
    private Integer contentId;

    @Min(1)
    @NotNull(message = "카테고리 아이디는 게시물을 저장하는데 반드시 필요한 요소입니다.")
    private Integer contentCategoryId;

    @Min(0)
    @Builder.Default
    private Integer viewCount = 0;

    @NotEmpty(message = "비밀번호는 게시물을 저장하는데 반드시 필요합니다.")
    @NotBlank(message = "빈칸은 유효한 비밀번호가 아닙니다.")
    @NotNull(message = "비밀번호가 없으면 게시물을 저장할 수 없습니다.")
    private String password;

    @NotEmpty(message = "게시물 내용은 저장에 반드시 필요한 요소입니다.")
    @NotBlank(message = "빈칸은 유효한 게시물 내용이 아닙니다.")
    @NotNull(message = "게시물 내용은 게시물 저장에 반드시 필요합니다.")
    @Size(min = 5 , max = 300 , message = "게시물 내용의 길이가 적절하지 않습니다.")
    private String content;

    @NotEmpty(message = "게시물 제목이 없으면 게시물을 저장할 수 없습니다.")
    @NotBlank(message = "빈칸은 유효한 게시글 제목이 아닙니다.")
    @NotNull(message = "게시물 제목은 저장에 반드시 필요한 요소입니다.")
    @Size(min=3 , max=18 , message = "게시물 제목의 길이가 적절하지 않습니다.")
    private String title;

    @NotEmpty(message = "게시글 작성자는 반드시 존재해야합니다.")
    @NotBlank(message = "빈칸은 유효한 게시물 작성자가 아닙니다.")
    @NotNull(message = "게시글 작성자는 반드시 존재해야합니다.")
    @Size(min = 2 , max = 10 , message = "게시글 작성자의 길이가 적절핳지 않습니다.")
    private String nickname;
}

