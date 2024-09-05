package com.bbansrun.project1.global.jwt;

import com.bbansrun.project1.domain.users.entity.Role;
import com.bbansrun.project1.domain.users.entity.User;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    // 사용자 UUID 반환
    public UUID getUserUuid() {
        return user.getUserUuid();
    }

    public List<String> getRoles() {
        return user.getRoles().stream()
            .map(Role::getAuthority)  // Role의 권한 문자열을 추출
            .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles();
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
        return true;  // 계정 만료 여부 (true: 만료되지 않음)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // 계정 잠금 여부 (true: 잠기지 않음)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // 자격 증명 만료 여부 (true: 만료되지 않음)
    }

    @Override
    public boolean isEnabled() {
        return true;  // 계정 활성화 여부 (true: 활성화)
    }
}
