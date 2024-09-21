package com.bbansrun.project1.domain.auth.controller;

import com.bbansrun.project1.domain.auth.dto.AuthResponse;
import com.bbansrun.project1.domain.auth.dto.LoginRequest;
import com.bbansrun.project1.domain.auth.dto.LoginResponse;
import com.bbansrun.project1.domain.auth.service.AuthService;
import com.bbansrun.project1.global.jwt.JwtProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtProperties jwtProperties;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,
        HttpServletResponse response) {

        // AuthService 통해 인증하고 JWT 토큰 생성
        LoginResponse loginResponse = authService.login(loginRequest);

        // JWT 토큰을 Authorization 헤더에 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + loginResponse.getJwtToken());

        // 리프레시 토큰을 HttpOnly 쿠키로 설정
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken",
                loginResponse.getRefreshToken())
            .httpOnly(true) // 클라이언트에서 접근 불가
            .secure(false) // HTTPS에서만 전송 -> 개발 환경에서는 false
            .path("/") // 루트 경로에서만 유효
            .maxAge(jwtProperties.getRefreshExpiration() / 1000) // 만료 시간 (밀리초 -> 초)
            .sameSite("Strict") // CSRF 방지
            .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        // JWT 토큰 + Refresh 토큰을 JSON 본문과 함께 반환
        return new ResponseEntity<>(loginResponse, headers, HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<?> info(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }

        try {
            AuthResponse authResponse = authService.getAuthInfo(authorizationHeader);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 검증 실패");
        }
    }

}
