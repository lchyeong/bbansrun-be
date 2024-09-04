package com.bbansrun.project1.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final Key secretKey;
    private final long validityInMilliseconds = 3600000; // 1 hour

    public JwtTokenProvider() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    /**
     * JWT 토큰 생성. user_uuid(고유 사용자 ID)와 역할 정보를 포함
     *
     * @param user_uuid 고유 사용자 ID (UUID)
     * @param roles     사용자 권한 (ROLE_USER, ROLE_ADMIN)
     * @return 생성된 JWT 토큰
     */
    public String createToken(UUID user_uuid, String[] roles) {
        // JWT 클레임에 사용자 정보 (UUID, 권한)를 추가
        Claims claims = Jwts.claims()
            .setSubject(user_uuid.toString()); // UUID를 문자열로 변환하여 subject에 넣음
        claims.put("roles", roles); // 권한 정보는 배열로 추가

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(secretKey)
            .compact();
    }

    /**
     * 토큰에서 user_uuid를 추출
     *
     * @param token JWT 토큰
     * @return UUID (고유 사용자 ID)
     */
    public UUID getUserUuid(String token) {
        String subject = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();

        return UUID.fromString(subject); // 토큰에서 추출한 subject는 UUID
    }

    /**
     * 토큰에서 권한 정보를 추출
     *
     * @param token JWT 토큰
     * @return 권한 정보 (String 배열)
     */
    public String[] getRoles(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();

        return (String[]) claims.get("roles");
    }

    /**
     * 토큰 유효성 검증
     *
     * @param token JWT 토큰
     * @return 토큰이 유효한지 여부
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Expired or invalid JWT token");
        }
    }
}
