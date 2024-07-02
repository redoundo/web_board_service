package com.study.connection.dto;

import lombok.*;

/**
 * 관리자가 댓글 삭제할 때 rest api 로 보내야 하는 내용.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeleteCommentDto {
    private Integer answerId;
    private String contentId;
}
