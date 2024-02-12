package com.study.connection.dto;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class UpdateConditionDto {
    @NotNull String contentId;
    @Nullable FilePartDto parts;
    @NotNull UpdateContentDto content;
    @Nullable ConditionDto condition;
}
