package com.study.connection.dto;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * ConditionDto 와 contentId 가 포함되어 있다.
 */
@Getter
@Setter
public class ConditionWithContentIdDto {
    @Nullable ConditionDto condition;
    @NotNull String contentId;
}
