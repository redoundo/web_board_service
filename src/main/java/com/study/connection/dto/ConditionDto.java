package com.study.connection.dto;

import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;

/**
 * 내용을 찾을 때 반영될 수 있는 검색 조건들.
 * 검색 시작 일시와 종료 일시 , 페이지에 null 값이 대입되면 기본 값으로 변경해서 진행한다.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConditionDto {
    @Nullable String keyword;
    @Nullable Integer contentCategoryId;
    @Nullable Date end;
    @Nullable Date start;
    @Nullable Integer page;
}
