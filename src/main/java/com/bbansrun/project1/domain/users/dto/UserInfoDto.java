package com.bbansrun.project1.domain.users.dto;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserInfoDto {

    private final UUID userUuid;
    private final List<String> roles;
}