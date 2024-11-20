package com.bbansrun.project1.domain.auth.service;

import static com.bbansrun.project1.global.util.CookieUtil.getRefreshTokenFromCookie;

import com.bbansrun.project1.domain.auth.dto.LoginRequest;
import com.bbansrun.project1.domain.auth.dto.LoginResponse;
import com.bbansrun.project1.domain.auth.repository.RefreshTokenRepository;
import com.bbansrun.project1.domain.users.dto.UserInfoDto;
import com.bbansrun.project1.domain.users.entity.User;
import com.bbansrun.project1.domain.users.repository.UserRepository;
import com.bbansrun.project1.global.exception.ApiException;
import com.bbansrun.project1.global.exception.ErrorCode;
import com.bbansrun.project1.global.jwt.CustomUserDetails;
import com.bbansrun.project1.global.jwt.JwtTokenParser;
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
import org.springframework.stereotype.Service;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenParser jwtTokenParser;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public LoginResponse login(LoginRequest loginRequest, HttpServletRequest request) {
        CustomUserDetails customUserDetails = authenticateUser(loginRequest);

        String jwtToken = createJwtToken(customUserDetails);
        String refreshToken = createRefreshToken(customUserDetails);

        User user = findUserByUuid(customUserDetails.getUserUuid());

        saveRefreshToken(user, refreshToken, getDeviceInfo(request));

        return buildLoginResponse(customUserDetails, jwtToken, refreshToken);
    }

    public void logout(HttpServletRequest request) {
        String refreshToken = getRefreshTokenFromCookie(request);

        if (refreshToken != null) {
            refreshTokenRepository.deleteByToken(refreshToken);
        }
    }

    public UserInfoDto getAuthInfo(String authorizationHeader) {
        String token = authorizationHeader.substring(7);

        UUID userUuid = jwtTokenParser.getUserUuid(token);
        String nickName = jwtTokenParser.getNickname(token);
        List<String> roles = jwtTokenParser.getRoles(token);

        return new UserInfoDto(userUuid, nickName, roles);
    }

    private LoginResponse buildLoginResponse(CustomUserDetails userDetails, String jwtToken, String refreshToken) {
        return new LoginResponse(
                jwtToken,
                refreshToken,
                userDetails.getNickName(),
                userDetails.getUserUuid().toString(),
                userDetails.getRoles()
        );
    }

    private void saveRefreshToken(User user, String refreshToken, String deviceInfo) {
        tokenService.saveRefreshToken(user, refreshToken, deviceInfo);
    }

    private User findUserByUuid(UUID userUuid) {
        return userRepository.findByUserUuid(userUuid)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    }

    private String createJwtToken(CustomUserDetails userDetails) {
        return jwtTokenProvider.createToken(
                userDetails.getUserUuid(),
                userDetails.getNickName(),
                userDetails.getRoles()
        );
    }

    private String createRefreshToken(CustomUserDetails userDetails) {
        return jwtTokenProvider.createRefreshToken(
                userDetails.getUserUuid(),
                userDetails.getNickName(),
                userDetails.getRoles()
        );
    }

    private String getDeviceInfo(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) {
            log.warn("User-Agent 헤더를 찾을 수 없습니다.");
            return "알 수 없는 장치";
        }
        return userAgent;
    }

    private CustomUserDetails authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        return (CustomUserDetails) authentication.getPrincipal();
    }
}
