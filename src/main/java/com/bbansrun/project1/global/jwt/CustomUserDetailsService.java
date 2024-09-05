package com.bbansrun.project1.service;

import com.bbansrun.project1.domain.users.entity.User;
import com.bbansrun.project1.domain.users.repository.UserRepository;
import com.bbansrun.project1.global.exception.ApiException;
import com.bbansrun.project1.global.exception.ErrorCode;
import com.bbansrun.project1.global.jwt.CustomUserDetails;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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

        return new CustomUserDetails(user);
    }

    /**
     * 사용자의 UUID를 받아서 사용자 정보를 조회하고, UserDetails로 변환하여 반환 JWT 토큰을 검증할 때 사용
     */
    public UserDetails loadUserByUuid(UUID userUuid) {
        User user = userRepository.findByUserUuid(userUuid)
            .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        return new CustomUserDetails(user);
    }
}
