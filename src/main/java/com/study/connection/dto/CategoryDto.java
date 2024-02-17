package com.study.connection.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Builder
@Setter
@Getter
public class CategoryDto {
    @NotNull Integer categoryId;
    @NotNull String categoryName;
}
