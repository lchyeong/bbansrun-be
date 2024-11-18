package com.bbansrun.project1.domain.auth.dto;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginResponse {

    private final String jwtToken;
    private final String refreshToken;
    private final String nickName;
    private final String userUuid;
    private final List<String> roles;
}