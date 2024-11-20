package com.bbansrun.project1.domain.auth.service;

import static com.bbansrun.project1.global.util.CookieUtil.getRefreshTokenFromCookie;

import com.bbansrun.project1.domain.auth.entity.RefreshToken;
import com.bbansrun.project1.domain.auth.repository.RefreshTokenRepository;
import com.bbansrun.project1.domain.users.entity.User;
import com.bbansrun.project1.global.exception.ApiException;
import com.bbansrun.project1.global.exception.ErrorCode;
import com.bbansrun.project1.global.jwt.JwtProperties;
import com.bbansrun.project1.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
                        jwtTokenProvider.getNickname(refreshToken),
                        jwtTokenProvider.getRoles(refreshToken)
                );
    }

    public void saveRefreshToken(User user, String refreshToken, String deviceInfo) {
        refreshTokenRepository.save(createRefreshToken(refreshToken, user, deviceInfo));
        throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);

    }

    private RefreshToken createRefreshToken(String refreshToken, User user, String deviceInfo) {
        return new RefreshToken(refreshToken,
                LocalDateTime.now().plus(jwtProperties.getRefreshExpiration(), ChronoUnit.MILLIS),
                deviceInfo,
                user,
                true
        );
    }
}
