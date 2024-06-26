package com.study.connection.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

/**
 * files 테이블 엔티티
 */
@Getter
@Setter
@Builder
@Validated
@AllArgsConstructor
@NoArgsConstructor
public class FileEntity {

    @Min(1)
    @NotNull
    private Integer contentIdHaveFile;

    @Min(1)
    @NotNull
    private Integer fileVolume;

    @NotBlank
    @NotNull
    private String fileName;

    @NotBlank
    @NotNull
    private String fileOriginalName;

    @NotBlank
    @NotNull
    private String filePath;

    @NotBlank
    @NotNull
    private String fileExtension;
}
