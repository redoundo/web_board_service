package com.study.connection.dto;

import jakarta.annotation.Nullable;
import lombok.*;
import org.springframework.validation.annotation.Validated;

/**
 * 입력할
 */
@Setter
@Getter
@Builder
@Validated
@AllArgsConstructor
@NoArgsConstructor
public class BoardContentDto {
    @Nullable
    private Integer boardId;
    private String title;
    private String content;
    private Integer categoryId;
    private String nickname;
}
