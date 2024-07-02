package com.study.connection.dto;

import jakarta.annotation.Nullable;
import lombok.*;

import java.util.Date;

/**
 * answer table 의 내용을 가져오는 dto
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Integer postId;
    @Nullable private Integer userId;
    private String nickname;
    private String answer;
    private Integer answerId;
    private Date submitDate;
}

