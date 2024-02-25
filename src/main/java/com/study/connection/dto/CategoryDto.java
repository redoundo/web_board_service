package com.study.connection.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDto {
    private Integer categoryId;
    private String categoryName;
}
