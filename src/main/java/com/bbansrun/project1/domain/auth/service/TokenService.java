package com.bbansrun.project1.domain.auth.service;

import static com.bbansrun.project1.global.util.CookieUtil.getRefreshTokenFromCookie;

import com.bbansrun.project1.domain.auth.entity.RefreshToken;
import com.bbansrun.project1.domain.auth.repository.RefreshTokenRepository;
import com.bbansrun.project1.domain.users.entity.User;
import com.bbansrun.project1.global.exception.ApiException;
import com.bbansrun.project1.global.exception.ErrorCode;
import com.bbansrun.project1.global.jwt.JwtProperties;
import com.bbansrun.project1.global.jwt.JwtTokenProvider;
import com.bbansrun.project1.global.util.HeaderUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    public String reissueAccessToken(HttpServletRequest request) {
        try {
            String refreshToken = getRefreshTokenFromCookie(request);

            if (refreshToken == null) {
                throw new ApiException(ErrorCode.REFRESH_TOKEN_INVALID);
            }
            if (!jwtTokenProvider.validateToken(refreshToken)) {
                throw new ApiException(ErrorCode.REFRESH_TOKEN_EXPIRED);
            }

            return jwtTokenProvider.createToken
                    (
                            jwtTokenProvider.getUserUuid(refreshToken),
                            jwtTokenProvider.getRoles(refreshToken)
                    );
        } catch (Exception e) {
            log.error("토큰 갱신 중 예상치 못한 오류가 발생했습니다.", e);
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public void saveRefreshToken(User user, String refreshToken, String deviceInfo) {
        try {
            refreshTokenRepository.save(createRefreshToken(refreshToken, user, deviceInfo));
        } catch (Exception e) {
            log.error("리프레시 토큰 저장 중 오류가 발생했습니다.", e);
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public RefreshToken createRefreshToken(String refreshToken, User user, String deviceInfo) {
        return new RefreshToken(refreshToken,
                LocalDateTime.now().plus(jwtProperties.getRefreshExpiration(), ChronoUnit.MILLIS),
                deviceInfo,
                user,
                true
        );
    }

    public ResponseEntity<String> buildTokenResponse(String token) {
        HttpHeaders headers = HeaderUtil.setTokenInHeader(token);
        return new ResponseEntity<>(token, headers, HttpStatus.OK);
    }
}
