package com.bbansrun.project1.domain.auth.service;

import com.bbansrun.project1.domain.auth.dto.AuthResponse;
import com.bbansrun.project1.domain.auth.dto.LoginRequest;
import com.bbansrun.project1.domain.auth.dto.LoginResponse;
import com.bbansrun.project1.domain.auth.entity.RefreshToken;
import com.bbansrun.project1.domain.auth.repository.RefreshTokenRepository;
import com.bbansrun.project1.domain.users.entity.User;
import com.bbansrun.project1.domain.users.repository.UserRepository;
import com.bbansrun.project1.global.jwt.CustomUserDetails;
import com.bbansrun.project1.global.jwt.JwtProperties;
import com.bbansrun.project1.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;

    public LoginResponse login(LoginRequest loginRequest, HttpServletRequest request) {
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

        // JWT 및 Refresh 토큰 생성
        String jwtToken = jwtTokenProvider.createToken(userUuid, roles);
        String refreshToken = jwtTokenProvider.createRefreshToken(userUuid, roles);

        // User-Agent를 기기 정보로 사용
        String deviceInfo = request.getHeader("User-Agent");
        log.info("Device info: {}", deviceInfo);
        saveRefreshToken(refreshToken, userUuid, deviceInfo);

        return new LoginResponse(jwtToken, refreshToken, userUuid.toString(), roles);
    }

    /**
     * Refresh 토큰을 이용하여 JWT 토큰 재발급
     *
     * @param refreshToken Refresh 토큰
     * @return 새로 발급된 JWT 토큰
     */
    public String refreshAccessToken(HttpServletRequest request) {
        String refreshToken = getRefreshTokenFromCookie(request);

        if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
            // 리프레시 토큰이 유효하면 새로운 액세스 토큰 발급
            UUID userUuid = jwtTokenProvider.getUserUuid(refreshToken);
            List<String> roles = jwtTokenProvider.getRoles(refreshToken);

            // 새로운 액세스 토큰 생성
            return jwtTokenProvider.createToken(userUuid, roles);
        } else {
            throw new IllegalArgumentException(
                "Refresh token expired or invalid. Please log in again.");
        }
    }

    /**
     * 리프레시 토큰을 데이터베이스에 저장 또는 업데이트
     *
     * @param token    리프레시 토큰
     * @param userUuid UUID로 사용자 조회 후 리프레시 토큰 저장
     */
    private void saveRefreshToken(String token, UUID userUuid, String deviceInfo) {
        Optional<User> userOptional = userRepository.findByUserUuid(userUuid);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // 빌더 패턴으로 리프레시 토큰 생성
            RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .expiryDate(Instant.now().plusMillis(jwtProperties.getRefreshExpiration()))
                .deviceInfo(deviceInfo)
                .user(user)
                .build();

            refreshTokenRepository.save(refreshToken);
        } else {
            throw new IllegalArgumentException("User not found with UUID: " + userUuid);
        }
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

    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
