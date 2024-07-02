package com.study.connection.dto;

import jakarta.annotation.Nullable;
import lombok.*;

/**
 * 문의 게시글 내용
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionSubmissionDto {
    private String title;
    private String content;
    private Boolean isLocked;
    @Nullable private Integer userId;
    @Nullable private Integer questionId;
    @Nullable private String answer;
}
