package com.study.connection.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

/**
 * comments 테이블 엔티티
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {
    private Integer commentId;
    private Integer commentedContentId;
    private String commentUser;
    private String comment;
    private Date commentedDate;
}

