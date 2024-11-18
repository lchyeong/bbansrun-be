package com.bbansrun.project1.global.jwt;

import com.bbansrun.project1.domain.users.entity.Role;
import com.bbansrun.project1.domain.users.entity.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    public UUID getUserUuid() {
        return user.getUserUuid();
    }

    public List<String> getRoles() {
        List<String> roles = new ArrayList<>();
        for (Role role : user.getRoles()) {
            roles.add(role.getAuthority());
        }
        return roles;
    }

    public String getNickName() {
        return user.getNickName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
