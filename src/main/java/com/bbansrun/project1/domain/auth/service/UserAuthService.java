package com.bbansrun.project1.domain.auth.service;

import static com.bbansrun.project1.domain.auth.util.CookieUtil.getRefreshTokenFromCookie;

import com.bbansrun.project1.domain.auth.dto.LoginRequest;
import com.bbansrun.project1.domain.auth.dto.LoginResponse;
import com.bbansrun.project1.domain.auth.repository.RefreshTokenRepository;
import com.bbansrun.project1.domain.users.dto.UserInfoDto;
import com.bbansrun.project1.domain.users.entity.User;
import com.bbansrun.project1.domain.users.repository.UserRepository;
import com.bbansrun.project1.global.exception.ApiException;
import com.bbansrun.project1.global.exception.ErrorCode;
import com.bbansrun.project1.global.jwt.CustomUserDetails;
import com.bbansrun.project1.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public LoginResponse login(LoginRequest loginRequest, HttpServletRequest request) {
        try {
            CustomUserDetails customUserDetails = authenticateUser(loginRequest);

            String jwtToken = jwtTokenProvider.createToken(customUserDetails.getUserUuid(),
                    customUserDetails.getRoles());
            String refreshToken = jwtTokenProvider.createRefreshToken(customUserDetails.getUserUuid(),
                    customUserDetails.getRoles());

            User user = userRepository.findByUserUuid(customUserDetails.getUserUuid())
                    .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

            tokenService.saveRefreshToken(user, refreshToken, getDeviceInfo(request));

            return new LoginResponse(jwtToken, refreshToken, customUserDetails.getUserUuid().toString(),
                    customUserDetails.getRoles());
        } catch (Exception e) {
            log.error("Login failed for email: {}", loginRequest.getEmail(), e);
            throw new ApiException(ErrorCode.INVALID_CREDENTIALS);
        }
    }

    public void logout(HttpServletRequest request) {
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

    public UserInfoDto getAuthInfo(String authorizationHeader) {
        try {
            String token = authorizationHeader.substring(7);

            UUID userUuid = jwtTokenProvider.getUserUuid(token);
            List<String> roles = jwtTokenProvider.getRoles(token);

            return new UserInfoDto(userUuid, roles);
        } catch (Exception e) {
            log.error("Error retrieving auth info", e);
            throw new ApiException(ErrorCode.UNAUTHORIZED);
        }
    }

    private String getDeviceInfo(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) {
            log.warn("User-Agent header is unknown");
            return "Unknown Device";
        }
        return userAgent;
    }

    private CustomUserDetails authenticateUser(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            return (CustomUserDetails) authentication.getPrincipal();
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", loginRequest.getEmail(), e);
            throw new ApiException(ErrorCode.INVALID_CREDENTIALS);
        }
    }
}
