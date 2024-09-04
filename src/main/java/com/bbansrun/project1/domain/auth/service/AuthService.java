package com.bbansrun.project1.domain.auth.service;

import com.bbansrun.project1.domain.auth.dto.LoginRequest;
import com.bbansrun.project1.global.jwt.JwtTokenProvider;
import com.bbansrun.project1.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 이메일과 비밀번호를 사용하여 인증하고 JWT 토큰을 생성하는 메서드
     *
     * @param loginRequest 로그인 요청 객체 (이메일과 비밀번호)
     * @return 생성된 JWT 토큰
     * @throws AuthenticationException 인증 실패 시 발생
     */
    public String authenticateAndCreateToken(LoginRequest loginRequest)
        throws AuthenticationException {
        // 사용자 인증 (이메일과 비밀번호 확인)
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );

        // 인증 성공 후 사용자 정보 로드
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // JWT 토큰 생성
        return jwtTokenProvider.createToken(
            userDetails.getUserUuid(),  // UUID 사용
            userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority()).toArray(String[]::new)
        );
    }

}
