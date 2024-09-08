package com.bbansrun.project1.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * JWT 토큰 생성. user_uuid(고유 사용자 ID)와 역할 정보를 포함
     *
     * @param userUuid 고유 사용자 ID (UUID)
     * @param roles    사용자 권한 (ROLE_USER, ROLE_ADMIN)
     * @return 생성된 JWT 토큰
     */
    public String createToken(UUID userUuid, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userUuid", userUuid.toString()); // UUID를 문자열로 변환하여 추가
        claims.put("roles", roles); // 역할 리스트 추가

        return Jwts.builder()
            .claims(claims)
            .issuedAt(new Date())    // 토큰 생성 시간
            .expiration(new Date(new Date().getTime() + jwtProperties.getExpiration())) // 토큰 만료 시간
            .signWith(getSigningKey()) // Key와 서명 알고리즘을 설정
            .compact(); // 토큰 생성
    }

    /**
     * 토큰에서 user_uuid를 추출
     *
     * @param token JWT 토큰
     * @return UUID (고유 사용자 ID)
     */
    public UUID getUserUuid(String token) {
        String userUuid = Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get("userUuid", String.class);
        return UUID.fromString(userUuid);
    }

    /**
     * 토큰에서 권한 정보를 추출
     *
     * @param token JWT 토큰
     * @return 권한 정보 (String 배열)
     */
    public List<String> getRoles(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(getSigningKey())  // Key로 서명 검증
            .build()
            .parseSignedClaims(token)
            .getPayload();

        List<?> roles = claims.get("roles", List.class);
        // List<?>를 List<String>으로 변환
        return roles.stream()
            .filter(role -> role instanceof String)  // String인지 확인
            .map(role -> (String) role)              // String으로 캐스팅
            .collect(Collectors.toList());
    }

    /**
     * 토큰 유효성 검증
     *
     * @param token JWT 토큰
     * @return 토큰이 유효한지 여부
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
