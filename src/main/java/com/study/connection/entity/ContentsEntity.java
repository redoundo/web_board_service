package com.study.connection.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * contents 테이블 엔티티
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContentsEntity {
    String password , nickname , title , content ;
    Integer contentId , contentCategoryId , viewCount;
    Date submitDate;
    Date updateDate;
    Boolean fileExistence;
}
