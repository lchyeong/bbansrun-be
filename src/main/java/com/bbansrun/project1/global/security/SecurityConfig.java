package com.bbansrun.project1.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

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
                    .anyRequest().authenticated() // 나머지 요청은 인증 필요
            )
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/api/public/**").permitAll() // /api/public/** 경로는 인증 없이 접근 허용
                    .requestMatchers("/api/admin/**")
                    .hasRole("ADMIN") // /api/admin/** 경로는 ADMIN 권한 필요
                    .requestMatchers("/api/user/**")
                    .hasAnyRole("USER", "ADMIN") // /api/user/** 경로는 USER 또는 ADMIN 권한 필요
                    .anyRequest().authenticated() // 나머지 모든 요청은 인증 필요
            )
            .formLogin(formLogin ->
                formLogin
                    .loginPage("/login")
                    .defaultSuccessUrl("/dashboard", true) // 로그인 성공 시 이동할 페이지
                    .permitAll()
            )
            .logout(logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout") // 로그아웃 후 이동할 페이지
                    .permitAll()
            )
            .sessionManagement(session -> session
                .sessionFixation().migrateSession() // 세션 고정 공격 방지
                .maximumSessions(1) // 하나의 사용자당 하나의 세션만 허용
                .expiredUrl("/login?expired")) // 세션 만료 시 이동할 페이지
            .rememberMe(rememberMe -> rememberMe
                .key("uniqueAndSecret") // Remember Me 기능 설정
                .tokenValiditySeconds(86400)); // Remember Me 기간 설정 (1일)
        return http.build();
    }
}
