package com.study.connection.entity;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

@Setter
@Getter
// contents 테이블 엔티티
public class ContentsEntity {
    String password , nickname , title , content ;
    Integer contentId , contentCategoryId , viewCount;
    Date submitDate;
    @Nullable Date updateDate;
    Boolean fileExistence;
}
