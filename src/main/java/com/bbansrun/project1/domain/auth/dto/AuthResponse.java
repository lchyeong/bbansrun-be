package com.bbansrun.project1.domain.auth.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {

    private String userUuid;
    private List<String> roles;
}
