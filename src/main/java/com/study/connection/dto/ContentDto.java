package com.study.connection.dto;

import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;

/**
 * modify.html , view.html 에 사용될 게시물의 내용 중 contents 테이블에 존재하는 내용 집합.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentDto {
    private Integer contentId;
    private Integer contentCategoryId;
    private Integer viewCount;
    private String password;
    private String content;
    private String title;
    private String nickname;
    private Date submitDate;
    private @Nullable Date updateDate;
}
