package com.bbansrun.project1.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private Key getSigningKey() {
        // SecretKey를 생성, HMAC-SHA 키를 위한 길이는 최소 256비트 이상이어야 합니다.
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    /**
     * JWT 토큰 생성. user_uuid(고유 사용자 ID)와 역할 정보를 포함
     *
     * @param userUuid 고유 사용자 ID (UUID)
     * @param roles    사용자 권한 (ROLE_USER, ROLE_ADMIN)
     * @return 생성된 JWT 토큰
     */
    public String createToken(UUID userUuid, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userUuid.toString());
        claims.put("roles", roles);

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getExpiration());

        return Jwts.builder()
            .setClaims(claims)  // 사용자 정보를 클레임으로 설정
            .setIssuedAt(now)    // 토큰 생성 시간
            .setExpiration(validity) // 토큰 만료 시간
            .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Key와 서명 알고리즘을 설정
            .compact(); // 토큰 생성
    }

    /**
     * 토큰에서 user_uuid를 추출
     *
     * @param token JWT 토큰
     * @return UUID (고유 사용자 ID)
     */
    public UUID getUserUuid(String token) {
        String userUuid = Jwts.parserBuilder()
            .setSigningKey(getSigningKey())  // Key로 서명 검증
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject(); // subject에서 UUID를 추출
        return UUID.fromString(userUuid);
    }

    /**
     * 토큰에서 권한 정보를 추출
     *
     * @param token JWT 토큰
     * @return 권한 정보 (String 배열)
     */
    public List<String> getRoles(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(getSigningKey())  // Key로 서명 검증
            .build()
            .parseClaimsJws(token)
            .getBody();

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
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false; // 토큰이 유효하지 않음
        }
    }
}
