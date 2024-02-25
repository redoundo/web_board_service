package com.study.connection.dto;

import com.study.connection.dto.file.FileDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import java.util.List;

/**
 * view.vue 에 사용될 내용 제공.
 */
@Getter
@Setter
@Builder
public class ViewPropsDto {

    private ContentDto contents;
    private String categoryName;
    private @Nullable List<FileDto> files;
    private @Nullable List<CommentDto> comments;
}
