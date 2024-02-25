package com.study.connection.dto.content;

import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;

/**
 * contents 를 select 할 때 반환되는 내용.
 * InsertContentsEntity 와 다르게 서브쿼리로 파일 존재 유무를 확인해주는 fileExistence 를 가지고 있다.
 * ViewContentDto 와 달리 password 를 포함하지 않는다.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SelectContentDto {
    private Integer contentId;
    private Integer contentCategoryId;
    private Integer viewCount;
    private String content;
    private String title;
    private String nickname;
    private Date submitDate;
    private @Nullable Date updateDate;
    private Boolean fileExistence;
}

