package com.bbansrun.project1.domain.auth.controller;

import com.bbansrun.project1.domain.auth.dto.JwtResponse;
import com.bbansrun.project1.domain.auth.dto.LoginRequest;
import com.bbansrun.project1.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest) {
        // AuthService 통해 인증하고 JWT 토큰 생성
        String jwtToken = authService.authenticateAndCreateToken(loginRequest);

        // JWT 토큰을 Authorization 헤더에 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        // JWT 토큰을 JSON 본문과 함께 반환
        return new ResponseEntity<>(new JwtResponse(jwtToken), headers, HttpStatus.OK);
    }
}
