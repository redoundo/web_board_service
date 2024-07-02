package com.study.connection.entity;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;

/**
 * comments 테이블 엔티티
 */
@Setter
@Getter
@Builder
@Validated
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity {
    @Min(1)
    @NotNull(message = "댓글 저장을 위해서는 댓글을 입력한 게시물의 아이디가 필요합니다.")
    private Integer postId;
    @Nullable private Integer userId;
    @NotBlank(message = "빈칸은 유효한 댓글이 아닙니다.")
    @NotNull(message = "댓글 내용은 댓글 저장에 반드시 필요한 내용입니다.")
    @Size(min = 3 , max = 399 , message = "댓글 길이가 적절하지 않습니다. 수정 후 다시 시도해주세요.")
    private String answer;
    private Integer postTable;
    @Nullable private Integer answerId;
}


