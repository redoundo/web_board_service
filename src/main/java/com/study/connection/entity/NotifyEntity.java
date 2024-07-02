package com.study.connection.entity;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NotifyEntity {
    @Nullable private Integer notifyId;
    private Integer categoryId;
    private String title;
    private String content;
    @Nullable private Boolean fixOnTop;
    @Nullable private Integer userId;
}
