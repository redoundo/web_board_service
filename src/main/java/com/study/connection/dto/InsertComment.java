package com.study.connection.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InsertComment {
    private String comment;
    private String postId;
}
