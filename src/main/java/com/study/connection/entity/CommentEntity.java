package com.study.connection.entity;

import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "comment_id" , nullable = false)
    private Integer commentId;
    @Column(name = "commented_content_id" , nullable = false)
    private Integer commentedContentId;
    @Column(name = "comment_user" , nullable = false)
    private String commentUser;
    @Column(name = "comment" , nullable = false)
    private String comment;
}

