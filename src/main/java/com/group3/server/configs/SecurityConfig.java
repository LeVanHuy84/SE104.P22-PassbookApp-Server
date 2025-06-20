package com.group3.server.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthenticationProvider provider;
    private final PreFilter filter;
    private final String[] WHITE_LIST = { "/api/v1/auth/login", "/api/v1/auth/register", "/api/v1/auth/forgot-password",
            "/api/v1/auth/logout", "/api/v1/auth/refresh-token", "/api/v1/auth/verify", "/api/v1/auth/send-otp" };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth -> {
            auth.requestMatchers(WHITE_LIST).permitAll().anyRequest().authenticated();
        }).sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(provider).addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
