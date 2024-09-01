package com.bbansrun.project1.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 인증 정보가 포함된 요청 허용
        config.addAllowedOrigin("http://localhost:5172"); // 신뢰할 수 있는 도메인만 허용
        config.addAllowedOrigin("https://another-domain.com"); // 추가 허용 도메인
        config.addAllowedHeader("Authorization"); // 필요한 헤더만 허용
        config.addAllowedHeader("Content-Type");
        config.addAllowedMethod("GET"); // 필요한 HTTP 메서드만 허용
        config.addAllowedMethod("POST");
        config.addAllowedMethod("OPTIONS");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 모든 경로에 대해 설정 적용

        return source;
    }
}
