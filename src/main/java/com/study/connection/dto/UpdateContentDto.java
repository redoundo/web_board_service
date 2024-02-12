package com.study.connection.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * content 내용 변경시 변경 가능한 내용만 제공. updateDate 는 default 로 값을 제공하므로 값을 넣지 말것!
 */
@Builder
@Getter
@Setter
public class UpdateContentDto {
    private String nickname;
    private String title;
    @NotNull private String password;
    private Integer contentCategoryId;
    @Builder.Default
    private Date updateDate = Date.valueOf(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("y-M-d H:m:s")));
}
