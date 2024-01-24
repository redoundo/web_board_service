package com.study.connection.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
// contents 테이블 엔티티
public class ContentsEntity {
    String password , nickname , title , content ;
    Integer contentId , contentCategoryId , viewCount;
    Date submitDate;
    Date updateDate;
    Boolean fileExistence;
}
