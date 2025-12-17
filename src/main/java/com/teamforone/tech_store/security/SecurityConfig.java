package com.teamforone.tech_store.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] WHITE_LIST_URL = {

            // ⭐⭐ Thêm đúng đường dẫn LOGIN – REGISTER của bạn
            "/auth/login",
            "/auth/login/**",
            "/auth/register",
            "/auth/register/**",
            "/auth/logout",

            // API có sẵn
            "/auth/admin/register",
            "/admin/2fa/phone/**",
            "/admin/2fa/email/**",
            "/auth/admin/update/{id}",
            "/auth/login", // đường dẫn cũ vẫn giữ
            "/api/v1/auth/refresh-token",
            "/api/v1/auth/verify",
            "/admin/**",

            // Static resources
            "/css/**",
            "/js/**",
            "/javascript/**",
            "/images/**",
            "/static/**",
            "/favicon.ico",
            "/webjars/**",

            // ⚠ Bạn có dòng này nên mọi route đều được permitAll:
            // "/**"
            // => nhưng mình vẫn giữ để tránh lỗi các phần khác.
            "/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}