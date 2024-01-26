package com.study.connection.dto;

import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;

@Builder
@Getter
public class ContentDto {
    String password ;
    String nickname ;
    String title;
    String content ;
    Integer contentId;
    Integer contentCategoryId ;
    Integer viewCount;
    Date submitDate;
    @Nullable Date updateDate;
    Boolean fileExistence;

    public String insertString() {
        return "content_category_id,password,view_count," +
                "content,nickname,title," +
                "submit_date FROM contents WHERE ";
    }
}
