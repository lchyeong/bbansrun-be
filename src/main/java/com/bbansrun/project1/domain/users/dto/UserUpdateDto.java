package com.bbansrun.project1.domain.users.dto;

import com.bbansrun.project1.domain.users.entity.Role;
import jakarta.validation.constraints.Email;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserUpdateDto {

    @Email
    private final String email;

    private final String password;

    private final String nickName;

    private final List<Role> roles;
}
