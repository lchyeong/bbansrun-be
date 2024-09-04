package com.bbansrun.project1.domain.users.dto;

import com.bbansrun.project1.domain.users.entity.Role;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthDTO {

    private UUID userUuid;
    private String email;
    private Set<Role> roles;
}
