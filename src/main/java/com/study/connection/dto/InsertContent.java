package com.study.connection.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@Builder
public class InsertContent {
    private Integer contentCategoryId;
    private String title;
    private String content;
    private String password;
    private String nickname;
    private Date submitDate;
    private Integer viewCount;
}
