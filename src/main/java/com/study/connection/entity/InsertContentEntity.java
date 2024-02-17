package com.study.connection.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

/**
 * contents 테이블에 insert 할 시, 사용되는 entity
 * 실제 contents 테이블에는 update_date 가 존재하나,
 * insert 하는데 update_date 가 존재할 이유가 없으므로 제거
 */
@Data
@Builder
@Getter
@Entity
@Table(name = "contents")
@NoArgsConstructor
@AllArgsConstructor
public class InsertContentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contentId")
    private Integer contentId;
    @Column(name = "contentCategoryId" , nullable = false)
    private Integer contentCategoryId;
    @Column(name = "viewCount" , nullable = false)
    private Integer viewCount;
    @Column(name = "password" , nullable = false)
    private String password;
    @Column(name = "content" , nullable = false)
    private String content;
    @Column(name = "title" , nullable = false)
    private String title;
    @Column(name = "nickname" , nullable = false)
    private String nickname;
    @Column(name = "submitDate" , nullable = false)
    private Date submitDate;
}

