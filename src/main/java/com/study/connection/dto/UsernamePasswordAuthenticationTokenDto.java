package com.study.connection.dto;

import lombok.*;
import org.springframework.lang.Nullable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsernamePasswordAuthenticationTokenDto {
    private String authority;
    private @Nullable Object details;
    private boolean authenticated;
    private String password;
    private String username;
    private @Builder.Default boolean accountNonExpired = true;
    private @Builder.Default boolean accountNonLocked = true;
    private @Builder.Default boolean credentialsNonExpired = true;
    private @Builder.Default boolean enabled = true;
    private String credentials;
    private String name;
}
