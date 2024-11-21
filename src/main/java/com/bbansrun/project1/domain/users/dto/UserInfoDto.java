package com.bbansrun.project1.domain.users.dto;

import com.bbansrun.project1.domain.users.entity.Role;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserInfoDto {

    private final UUID userUuid;
    private final String nickName;
    private final List<Role> roles;
}