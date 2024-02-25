package com.study.connection.dto.file;

import lombok.Builder;
import lombok.Getter;

/**
 * 파일 다운로드 혹은 삭제에 필요한 file_name , file_original_name , file_id , file_path 만 제공.
 */
@Getter
@Builder
public class FileDto {
    private Integer fileId;
    private String fileName;
    private String fileOriginalName;
    private String filePath;
}

