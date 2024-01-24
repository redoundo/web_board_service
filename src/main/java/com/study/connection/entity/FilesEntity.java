package com.study.connection.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FilesEntity {
    String fileName , filePath , fileExtension , fileOriginalName;
    Integer contentIdHaveFile , fileId , fileVolume;
}
