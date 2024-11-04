package com.bbansrun.project1.domain.auth.service;

import com.bbansrun.project1.domain.auth.dto.AuthResponse;
import com.bbansrun.project1.domain.auth.dto.LoginRequest;
import com.bbansrun.project1.domain.auth.dto.LoginResponse;
import com.bbansrun.project1.domain.auth.entity.RefreshToken;
import com.bbansrun.project1.domain.auth.repository.RefreshTokenRepository;
import com.bbansrun.project1.domain.users.entity.User;
import com.bbansrun.project1.domain.users.repository.UserRepository;
import com.bbansrun.project1.global.exception.ApiException;
import com.bbansrun.project1.global.exception.ErrorCode;
import com.bbansrun.project1.global.jwt.CustomUserDetails;
import com.bbansrun.project1.global.jwt.JwtProperties;
import com.bbansrun.project1.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;

    public LoginResponse loginService(LoginRequest loginRequest, HttpServletRequest request) {
        try {
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
        } catch (Exception e) {
            log.error("Login failed for email: {}", loginRequest.getEmail(), e);
            throw new ApiException(ErrorCode.INVALID_CREDENTIALS);
        }
    }


    public void logoutService(HttpServletRequest request) {
        try {
            String refreshToken = getRefreshTokenFromCookie(request);

            if (refreshToken != null) {
                refreshTokenRepository.deleteByToken(refreshToken);
            }
        } catch (Exception e) {
            log.error("Error during logout", e);
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Refresh 토큰을 이용하여 JWT 토큰 재발급 return 새로 발급된 JWT 토큰
     */
    public String refreshAccessToken(HttpServletRequest request) {
        try {
            String refreshToken = getRefreshTokenFromCookie(request);

            if (refreshToken == null) {
                throw new ApiException(ErrorCode.REFRESH_TOKEN_INVALID);
            }
            if (!jwtTokenProvider.validateToken(refreshToken)) {
                throw new ApiException(ErrorCode.REFRESH_TOKEN_EXPIRED);
            }

            // 리프레시 토큰이 유효하면 새로운 액세스 토큰 발급
            UUID userUuid = jwtTokenProvider.getUserUuid(refreshToken);
            List<String> roles = jwtTokenProvider.getRoles(refreshToken);

            return jwtTokenProvider.createToken(userUuid, roles);
        } catch (ApiException e) {
            log.error("Refresh token invalid or expired.", e);
            throw e;  // 이미 적절한 ApiException이 발생했으므로 다시 던짐
        } catch (Exception e) {
            log.error("Unexpected error during token refresh.", e);
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 리프레시 토큰을 데이터베이스에 저장 또는 업데이트
     *
     * @param token    리프레시 토큰
     * @param userUuid UUID로 사용자 조회 후 리프레시 토큰 저장
     */
    private void saveRefreshToken(String token, UUID userUuid, String deviceInfo) {
        try {
            User user = userRepository.findByUserUuid(userUuid)
                    .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

            RefreshToken refreshToken = new RefreshToken(token,
                    LocalDateTime.now().plus(jwtProperties.getRefreshExpiration(), ChronoUnit.MILLIS),
                    deviceInfo, user, true);

            refreshTokenRepository.save(refreshToken);
        } catch (Exception e) {
            log.error("Error saving refresh token for user UUID: {}", userUuid, e);
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public AuthResponse getAuthInfo(String authorizationHeader) {
        try {
            String token = authorizationHeader.substring(7); // 'Bearer ' 이후의 토큰 추출

            // 토큰에서 UUID와 roles 추출
            UUID userUuid = jwtTokenProvider.getUserUuid(token);
            List<String> roles = jwtTokenProvider.getRoles(token);

            // 응답 데이터 생성
            AuthResponse authResponse = new AuthResponse();
            authResponse.setUserUuid(userUuid.toString());
            authResponse.setRoles(roles);

            return authResponse;
        } catch (Exception e) {
            log.error("Error retrieving auth info", e);
            throw new ApiException(ErrorCode.UNAUTHORIZED);
        }
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
