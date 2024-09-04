package com.bbansrun.project1.service;

import com.bbansrun.project1.domain.users.entity.Role;
import com.bbansrun.project1.domain.users.entity.User;
import com.bbansrun.project1.domain.users.repository.UserRepository;
import com.bbansrun.project1.global.exception.ApiException;
import com.bbansrun.project1.global.exception.ErrorCode;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 사용자의 이메일을 받아서 사용자 정보를 조회하고, UserDetails로 변환하여 반환 최초 로그인시 사용
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),      // 사용자의 이메일을 username처럼 사용
            user.getPassword(),
            mapRolesToAuthorities(user.getRoles())  // 권한 정보를 변환하여 반환
        );
    }

    /**
     * 사용자의 UUID를 받아서 사용자 정보를 조회하고, UserDetails로 변환하여 반환 JWT 토큰을 검증할 때 사용
     */
    public UserDetails loadUserByUuid(UUID userUuid) {
        User user = userRepository.findByUserUuid(userUuid)
            .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        return new org.springframework.security.core.userdetails.User(
            user.getUserUuid().toString(),
            user.getPassword(),
            mapRolesToAuthorities(user.getRoles())  // 권한 정보를 변환하여 반환
        );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        // roles를 GrantedAuthority로 변환
        return roles.stream()
            .map(role -> new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return role.getAuthority();
                }
            })
            .collect(Collectors.toSet());
    }
}
