package com.study.connection.entity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FileEntity {
    String fileName ;
    String filePath ;
    String fileExtension;
    String fileOriginalName;
    Integer contentIdHaveFile ;
    Integer fileId ;
    Integer fileVolume;
}
