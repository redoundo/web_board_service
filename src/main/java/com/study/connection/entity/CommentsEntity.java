package com.study.connection.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentsEntity {
    int commentId , commentedContentId;
    String commentUser , comment;
    Date commentedDate;
}
