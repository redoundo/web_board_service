package com.study.connection.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Notification {
    private Integer categoryId;
    private String title;
    private String content;
    private Boolean fixOnTop;
}

