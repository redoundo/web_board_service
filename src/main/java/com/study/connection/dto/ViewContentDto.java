package com.study.connection.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;

/**
 * @see ContentFullDetails 의 요소 중 하나로 사용된다.
 * @see SelectContentDto 와 달리 fileExistence 를 가지고 있지 않지만
 * 내용 조작을 위한 password 를 포함하고 있다.
 */
@Builder
@Setter
@Getter
public class ViewContentDto {

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
