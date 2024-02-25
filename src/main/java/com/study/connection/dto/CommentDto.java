package com.study.connection.dto;

import lombok.*;

/**
 * 댓글 표시에 필요한 내용. contentId로 찾은 내용을 반환할 때 사용 하거나 댓글을 저장하려고 할 때, commentId 가 없을 경우에 사용.
 * comment 를 사용자가 직접 삭제 , 수정하는 기능은 제공하지 않으므로 ,CommentsEntity 내용에서 comment_id를 제거했다.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Integer commentedContentId;
    private String commentUser;
    private String comment;
}

