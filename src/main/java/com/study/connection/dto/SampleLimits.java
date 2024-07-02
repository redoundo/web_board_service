package com.study.connection.dto;

import lombok.*;

/**
 * main 페이지에서 가져와야 하는 게시글의 개수.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SampleLimits {
    private Integer notify;
    private Integer question;
    private Integer board;
    private Integer gallery;
}
