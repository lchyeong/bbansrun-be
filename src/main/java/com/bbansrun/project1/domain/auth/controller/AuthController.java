package com.bbansrun.project1.domain.auth.controller;

import static com.bbansrun.project1.global.util.HeaderUtil.setTokenInHeader;

import com.bbansrun.project1.domain.auth.dto.LoginRequest;
import com.bbansrun.project1.domain.auth.dto.LoginResponse;
import com.bbansrun.project1.domain.auth.service.TokenService;
import com.bbansrun.project1.domain.auth.service.UserAuthService;
import com.bbansrun.project1.domain.users.dto.UserInfoDto;
import com.bbansrun.project1.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    private final TokenService tokenService;
    private final UserAuthService userAuthService;
    private final CookieUtil cookieUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        LoginResponse loginResponse = userAuthService.login(loginRequest, request);
        cookieUtil.addRefreshTokenCookie(response, loginResponse.getRefreshToken());
        HttpHeaders headers = setTokenInHeader(loginResponse.getJwtToken());
        return new ResponseEntity<>(loginResponse, headers, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        userAuthService.logout(request);
        cookieUtil.expireRefreshTokenCookie(response);
        return ResponseEntity.ok("로그아웃 성공");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
        String newAccessToken = tokenService.reissueAccessToken(request);
        HttpHeaders headers = setTokenInHeader(newAccessToken);
        return new ResponseEntity<>("토큰 재발급에 성공했습니다.", headers, HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<?> info(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }
        UserInfoDto userInfoDto = userAuthService.getAuthInfo(authorizationHeader);
        return ResponseEntity.ok(userInfoDto);
    }
}