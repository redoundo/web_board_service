package com.study.connection.dto;

import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 특정 contentId 의 자세한 내용을 반환해야할 때 사용된다.
 * 내용과 해당 id 로 존재하는 파일들 , 댓글들을 포함한다.
 */
@Getter
@Builder
public class ContentFullDetails {
    ViewContentDto content;
    @Nullable List<CommentDto> comments;
    @Nullable List<FileDto> files;
}
