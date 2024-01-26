package com.study.connection.entity;

import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
/**
 *contents 테이블 엔티티
 */
@Builder
@Getter
public class ContentEntity {
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
}
