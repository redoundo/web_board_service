package com.study.connection.dto;

import com.study.connection.entity.FileEntity;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 *  FileEntity 중에 file_name , file_path 만 제공.
 */
@Getter
public class FileDto {
    String fileName;
    String filePath;
    Integer fileId;

    FileDto (@NotNull FileEntity entity) {
        this.fileName = entity.getFileName();
        this.filePath = entity.getFilePath();
        this.fileId = entity.getFileId();
    }

    public FileDto(@NotNull String name , @NotNull String path , @NotNull Integer id) {
        this.filePath = path;
        this.fileName = name;
        this.fileId = id;
    }
}
