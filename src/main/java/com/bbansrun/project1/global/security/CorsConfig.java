package com.bbansrun.project1.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    //스프링 부트 2.4부터는 WebMvcConfigurer를 사용하지 않고 CorsConfigurationSource를 사용한다.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 쿠키허용?
        config.addAllowedOriginPattern("*"); // 모든 ip에 대해 응답 허용
        config.addAllowedHeader("*"); // 모든 header에 대해 요청 허용
        config.addAllowedMethod("*"); // GET, POST, PUT, DELETE, PATCH, OPTIONS 모든 HTTP메소드 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}

