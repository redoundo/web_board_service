package com.study.connection.dto.file;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class DownloadFileInfo {
    @NotNull
    @NotBlank
    private String fileName;
    @NotNull
    @NotBlank
    private String filePath;
}
