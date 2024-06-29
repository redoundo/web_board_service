package com.study.connection.entity;
import lombok.*;

/**
 * files 테이블 엔티티
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileEntity {
    private Integer fileId;
    private Integer contentIdHaveFile;
    private Integer fileVolume;
    private String fileName;
    private String fileOriginalName;
    private String filePath;
    private String fileExtension;
}
