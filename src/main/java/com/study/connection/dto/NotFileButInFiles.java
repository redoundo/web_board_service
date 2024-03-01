package com.study.connection.dto;

import com.study.connection.dto.file.FileDto;
import lombok.*;
import org.jetbrains.annotations.Nullable;

/**
 * files 테이블에는 존재하지만 multiPart 형식이 아닌 filePath , fileId , fileName 등을 가지고 있는 form input 들.
 * 최대 3개.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotFileButInFiles {
    @Nullable FileDto notFile1;
    @Nullable FileDto notFile2;
    @Nullable FileDto notFile3;
}
