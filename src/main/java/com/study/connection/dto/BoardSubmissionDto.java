package com.study.connection.dto;

import jakarta.annotation.Nullable;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 변경할 자유 게시글 내용
 */
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@MultipartConfig
public class BoardSubmissionDto {
    private String title;
    private String content;
    private Integer categoryId;
    private Integer postTable;
    private @Nullable Integer userId;
    private @Nullable Integer boardId;
    private @Nullable List<Integer> images;
    private @Nullable List<MultipartFile> files;
}
