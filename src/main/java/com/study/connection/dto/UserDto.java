package com.study.connection.dto;

import jakarta.annotation.Nullable;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String id;
    private String password;
    @Nullable private String name;
}
