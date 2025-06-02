// src/main/java/org/dfpl/lecture/db/backend/config/SecurityConfig.java

package org.dfpl.lecture.db.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // (1) CORS 필요 시 추가 설정
                .cors(cors -> {})

                // (2) REST API 이므로 CSRF 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                // (3) URL 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // ──────────────────────────────────────────────────────────────
                        // (A) 검색, 상세조회, 인기 조회 API: 인증 없이 모두 허용
                        // ──────────────────────────────────────────────────────────────
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/movies/search").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/movies/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/popular/**").permitAll()

                        // ──────────────────────────────────────────────────────────────
                        // (B) 리뷰 작성/삭제 등: USER 권한 필요
                        // ──────────────────────────────────────────────────────────────
                        .requestMatchers(HttpMethod.POST, "/api/reviews/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/reviews/**").hasRole("USER")

                        // ──────────────────────────────────────────────────────────────
                        // (C) 내 정보 조회: USER 권한 필요
                        // ──────────────────────────────────────────────────────────────
                        .requestMatchers(HttpMethod.GET, "/api/users/me", "/api/users/me/**")
                        .hasRole("USER")

                        // ──────────────────────────────────────────────────────────────
                        // (D) 그 외 모든 요청: 인증된 사용자만
                        // ──────────────────────────────────────────────────────────────
                        .anyRequest().authenticated()
                )

                // (4) JWT 방식: 세션 생성 안 함
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // (5) JWT 필터 등록 (UsernamePasswordAuthenticationFilter 앞에)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // PasswordEncoder, AuthenticationManager 빈 등록 (기존 코드 그대로)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
}
