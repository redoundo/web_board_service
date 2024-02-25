package com.study.connection.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ThingsForGetContentId {
    private String title;
    private String content;
    private Integer contentCategoryId;
}
