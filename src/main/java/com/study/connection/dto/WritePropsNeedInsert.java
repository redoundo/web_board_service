package com.study.connection.dto;

import com.study.connection.dto.file.FilePartDto;
import com.study.connection.entity.InsertContentEntity;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WritePropsNeedInsert {
    private @NotNull InsertContentEntity content;
    private @Nullable FilePartDto files;
}
