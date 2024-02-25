package com.study.connection.dto;

import com.study.connection.dto.content.SelectContentDto;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Data
@Builder
public class AllIndexPropsDto {
    List<SelectContentDto> contents;
    @NotNull Integer total;
    @NotNull List<CategoryDto> categories;
}
