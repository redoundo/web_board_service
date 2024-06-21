package com.study.connection.entity;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class FilesEntity {
    private String fileName;
    private String filePath;
    private String fileExtension;
    private String fileOriginalName;
    private Integer contentIdHaveFile;
    private Integer fileId;
    private Integer fileVolume;
}
