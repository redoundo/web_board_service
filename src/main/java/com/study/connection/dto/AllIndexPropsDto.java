package com.study.connection.dto;

import com.study.connection.dto.content.SelectContentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllIndexPropsDto {
    List<SelectContentDto> contents;
    @NotNull Integer total;
    @NotNull List<CategoryDto> categories;
}
