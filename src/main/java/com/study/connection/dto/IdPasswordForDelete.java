package com.study.connection.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@AllArgsConstructor
@NoArgsConstructor
public class IdPasswordForDelete {

    @NotNull(message = "게시글 삭제를 위해서 게시글 아이디가 필요합니다.")
    @Min(1)
    private Integer contentId;

    @NotNull(message = "게시글 삭제는 사용자 인증 후 진행됩니다.")
    @NotEmpty(message = "게시글 삭제는 사용자 인증 후 진행됩니다.")
    @NotBlank(message = "빈칸은 유효한 비밀번호가 아닙니다.")
    private String password;
}
