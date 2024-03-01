package com.study.connection.dto.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 파일 다운로드 혹은 삭제에 필요한 file_name , file_original_name , file_id , file_path 만 제공.
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {
    private Integer fileId;
    private String fileName;
    private String fileOriginalName;
    private String filePath;
}

