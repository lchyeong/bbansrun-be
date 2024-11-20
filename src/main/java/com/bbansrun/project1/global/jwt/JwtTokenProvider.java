package com.bbansrun.project1.global.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

    public String createToken(UUID userUuid, String nickName, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userUuid", userUuid.toString());
        claims.put("roles", roles);
        claims.put("nickName", nickName);

        Date now = new Date();
        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + jwtProperties.getExpiration()))
                .signWith(getSigningKey())
                .compact();
    }

    public String createRefreshToken(UUID userUuid, String nickName, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userUuid", userUuid.toString());
        claims.put("roles", roles);
        claims.put("nickName", nickName);

        Date now = new Date();
        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(
                        new Date(now.getTime() + jwtProperties.getRefreshExpiration()))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
