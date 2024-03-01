package com.study.connection.dto;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckPassword {
    private String password;
    private String original;
}
