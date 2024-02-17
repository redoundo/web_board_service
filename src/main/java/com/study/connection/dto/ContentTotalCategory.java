package com.study.connection.dto;

import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 검색 조건이 있다면 검색 조건과 페이지 네이션이 반영된 contents 테이블의 내용들.
 * 페이지네이션이 반영되면 존재하는 내용의 갯수를 알 수 없으므로 따로 구하여 반환해준다.
 */
@Builder
@Getter
public class ContentTotalCategory {
    List<SelectContentDto> contents;
    @NotNull Integer total;
    @NotNull List<CategoryDto> categories;
}
