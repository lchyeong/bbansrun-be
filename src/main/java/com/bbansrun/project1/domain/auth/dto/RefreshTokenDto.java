package com.bbansrun.project1.domain.auth.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RefreshTokenDto {
    private final String token;
    private final String deviceInfo;
    private final LocalDateTime expirationTime;
}
