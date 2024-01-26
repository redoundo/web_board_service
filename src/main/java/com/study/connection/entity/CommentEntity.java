package com.study.connection.entity;

import lombok.Builder;
import lombok.Getter;

import java.sql.Date;

@Builder
@Getter
public class CommentEntity {
    Integer commentId ;
    Integer commentedContentId;
    String commentUser ;
    String comment;
    Date commentedDate;
}
