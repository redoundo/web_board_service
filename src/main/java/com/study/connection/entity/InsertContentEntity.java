package com.study.connection.entity;
import lombok.*;

import java.sql.Date;

/**
 * contents 테이블에 insert 할 시, 사용되는 entity
 * 실제 contents 테이블에는 update_date 가 존재하나,
 * insert 하는데 update_date 가 존재할 이유가 없으므로 제거
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertContentEntity {
    private Integer contentId;
    private Integer contentCategoryId;
    private Integer viewCount;
    private String password;
    private String content;
    private String title;
    private String nickname;
    private Date submitDate;
}

