package com.study.connection.dto;

import lombok.*;

/**
 * 게시글 삭제할 떄 사용
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteContentDto {
    private Integer userId;
    private String contentId;
}
