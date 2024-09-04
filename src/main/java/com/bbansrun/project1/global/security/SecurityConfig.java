package com.bbansrun.project1.global.security;

import com.bbansrun.project1.global.jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource)) // CORS 설정
            .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
            .headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin) // H2 콘솔 허용
                .contentSecurityPolicy(csp -> csp
                        .policyDirectives("script-src 'self'; object-src 'none';")
                    // Content Security Policy 설정
                )
            )
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/h2-console/**").permitAll() // H2 콘솔 접근 허용
                    .requestMatchers("/public/**").permitAll() // 공개 리소스 허용
                    .requestMatchers("/api/public/**").permitAll() // /api/public/** 경로는 인증 없이 접근 허용
                    .requestMatchers("/api/admin/**")
                    .hasRole("ADMIN") // /api/admin/** 경로는 ADMIN 권한 필요
                    .requestMatchers("/api/user/**")
                    .hasAnyRole("USER", "ADMIN") // /api/user/** 경로는 USER 또는 ADMIN 권한 필요
                    .anyRequest().authenticated() // 나머지 모든 요청은 인증 필요
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션을 사용하지 않음 (stateless)
            .addFilterBefore(jwtTokenFilter,
                UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
