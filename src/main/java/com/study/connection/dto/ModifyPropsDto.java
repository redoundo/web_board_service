package com.study.connection.dto;

import com.study.connection.dto.file.FileDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import java.util.List;

/**
 * modify.vue 에 필요한 내용 집합
 */
@Getter
@Setter
@Builder
public class ModifyPropsDto {

    private ContentDto content;
    private String categoryName;
    private @Nullable List<FileDto> files;
}
