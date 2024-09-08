package com.bbansrun.project1.domain.auth.service;

import com.bbansrun.project1.domain.auth.dto.AuthResponse;
import com.bbansrun.project1.domain.auth.dto.LoginRequest;
import com.bbansrun.project1.domain.auth.dto.LoginResponse;
import com.bbansrun.project1.global.jwt.CustomUserDetails;
import com.bbansrun.project1.global.jwt.JwtTokenProvider;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest loginRequest) {
        // 사용자 인증 - 실패 시 AuthenticationException이 자동으로 던져짐
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                loginRequest.getPassword())
        );

        // 인증 성공 후 사용자 정보를 가져옴
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;

        // UUID 기반으로 JWT 토큰 발급
        UUID userUuid = customUserDetails.getUserUuid();
        List<String> roles = customUserDetails.getRoles();
        String jwtToken = jwtTokenProvider.createToken(userUuid, roles);

        // JwtResponse 객체로 사용자 정보와 토큰 반환
        return new LoginResponse(jwtToken, userUuid.toString(), roles);
    }

    public AuthResponse getAuthInfo(String authorizationHeader) {
        String token = authorizationHeader.substring(7); // 'Bearer ' 이후의 토큰 추출

        // 토큰에서 UUID와 roles 추출
        UUID userUuid = jwtTokenProvider.getUserUuid(token);
        List<String> roles = jwtTokenProvider.getRoles(token);

        // 응답 데이터 생성
        AuthResponse authResponse = new AuthResponse();
        authResponse.setUserUuid(userUuid.toString());
        authResponse.setRoles(roles);

        return authResponse;
    }

}
