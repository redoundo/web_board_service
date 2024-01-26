package com.study.connection.dto;

import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 검색조건. start , end 는 디폴트 값으로 검색조건을 설정하지 않으면 설정된 값으로 진행.
 */
@Getter
@Builder
public class SearchOptionDto {
    @Nullable String keyword;
    @Nullable Integer categoryId;
    @Nullable Integer contentId;

    @Builder.Default @Nullable
    Date end = Date.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("y-M-d")));
    @Builder.Default @Nullable
    Date start = Date.valueOf(LocalDateTime.now().minusYears(1).format(DateTimeFormatter.ofPattern("y-M-d")));
    @Builder.Default @Nullable
    Integer page = 1;

    /*
        sql 문의 select 할 내용을 제공.
     */
    public String fullString() {
        return "content_id,content_category_id,password," +
                "view_count,content,nickname,title," +
                "submit_date,update_date," +
                "(SELECT IF ( COUNT(*)>0 , 'true' , 'false' ) FROM files WHERE files.content_id_have_file=" +
                "contents.content_id) AS file_existence ";
    }
}
