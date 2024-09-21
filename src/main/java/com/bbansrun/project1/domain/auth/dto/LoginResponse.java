package com.bbansrun.project1.domain.auth.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String jwtToken;
    private String refreshToken;
    private String userUuid;
    private List<String> roles;

}