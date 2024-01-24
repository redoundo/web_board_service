package com.study.connection.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentsEntity {
    int commentId , commentedContentId;
    String commentUser , comment;
    Date commentedDate;
}
