package com.study.connection.dto.file;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;

/**
 * request 에서 받아온 파일 Part 들.
 */
@Getter
@Setter
public class FilePartDto {
    @Nullable MultipartFile file1;
    @Nullable MultipartFile file2;
    @Nullable MultipartFile file3;
}