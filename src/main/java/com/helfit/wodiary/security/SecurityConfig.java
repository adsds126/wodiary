package com.helfit.wodiary.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 설정 비활성화
                .csrf(csrf -> csrf.disable())
                // 요청에 대한 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 회원가입 경로는 인증 없이 접근 허용
                        .requestMatchers("/api/v1/**").permitAll()
                        // 기타 모든 요청은 인증 필요
                        .anyRequest().authenticated());

        return http.build();
    }
}
