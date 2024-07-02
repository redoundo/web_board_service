package com.study.connection.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * 검색 결과와 총 개수, 카테고리들 반환.
 */
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultDto {
    private List<CategoryDto> categories;
    private Integer total;
    private List<Map<String,Object>> results;
}
