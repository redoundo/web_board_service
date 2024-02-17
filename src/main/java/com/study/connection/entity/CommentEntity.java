package com.study.connection.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

/**
 * comments 테이블 엔티티
 */
@Entity
@Data
@Builder
@Getter
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentId" , nullable = false)
    private Integer commentId;
    @Column(name = "commentedContentId" , nullable = false)
    private Integer commentedContentId;
    @Column(name = "commentUser" , nullable = false)
    private String commentUser;
    @Column(name = "comment" , nullable = false)
    private String comment;
    @Column(name= "commentedDate" , nullable = false)
    private Date commentedDate;
}

