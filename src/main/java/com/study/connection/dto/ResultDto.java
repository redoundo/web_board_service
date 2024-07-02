package com.study.connection.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * 게시판에서 검색시, 페이지네이션 된 검색 결과와 검색 결과의 총 개수 포함.
 */
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResultDto {
    @Nullable
    private List<Map<String, Object>> results;
    @NotNull
    private Integer total;
}
