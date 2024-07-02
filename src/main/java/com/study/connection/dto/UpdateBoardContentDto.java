package com.study.connection.dto;

import lombok.*;

/**
 * boards 테이블 변경시 필요한 내용.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBoardContentDto {
    private String title;
    private String content;
    private Integer categoryId;
    private Integer boardId;
}
