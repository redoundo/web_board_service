package com.study.connection.dto;

import jakarta.annotation.Nullable;
import lombok.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 게시판에서 검색을 할때 사용된다.
 */
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchConditionDto {
    @Nullable
    private String keyword;
    @Nullable private Integer categoryId;
    @Builder.Default
    @Nullable private Date start = Date.valueOf(LocalDateTime.now().minusYears(1).format(DateTimeFormatter.ofPattern("yyyy-M-dd")));
    @Builder.Default
    @Nullable private Date end = Date.valueOf(LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-M-dd")));
    @Builder.Default
    @Nullable private Integer maxCount = 20;
    @Builder.Default
    @Nullable private String orderByColumn = "submitDate";
    @Builder.Default
    @Nullable private Boolean orderByDesc = false;
    @Builder.Default
    @Nullable private Integer page = 0;
    @Nullable private Integer userId;
}