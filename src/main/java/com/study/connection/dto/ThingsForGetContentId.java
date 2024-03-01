package com.study.connection.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThingsForGetContentId {
    private String title;
    private String content;
    private Integer contentCategoryId;
}
