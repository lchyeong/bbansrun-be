package com.bbansrun.project1.service;

import com.bbansrun.project1.domain.users.entity.User;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public record CustomUserDetails(User user) implements UserDetails {

    public UUID getUserUuid() {
        return user.getUserUuid();  // UUID를 반환
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 반환
        return user.getRoles().stream()
            .map(role -> (GrantedAuthority) role)  // Role을 GrantedAuthority로 변환
            .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPassword();  // 비밀번호 반환
    }

    @Override
    public String getUsername() {
        return user.getEmail();  // 이메일 반환
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // 계정이 만료되지 않음
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // 계정이 잠기지 않음
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // 자격 증명이 만료되지 않음
    }

    @Override
    public boolean isEnabled() {
        return true;  // 계정 활성화 여부
    }
}
