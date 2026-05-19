package com.example.HotelManagementSystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.
UsernamePasswordAuthenticationFilter;

import com.example.HotelManagementSystem.security.JwtFilter;

@Configuration
public class SecurityConfig {

    // =========================
    // JWT FILTER INJECTION
    // =========================

    @Autowired
    private JwtFilter jwtFilter;

    // =========================
    // SECURITY FILTER CHAIN
    // =========================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http

            // Disable CSRF
            .csrf(csrf -> csrf.disable())

            // Stateless Session
            .sessionManagement(session ->

                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )

            // Authorization Rules
            .authorizeHttpRequests(auth -> auth

                    // Public Routes
                    .requestMatchers(
                            "/api/auth/**"
                    )
                    .permitAll()

                    // Admin Routes
                    .requestMatchers(
                            "/api/admin/**"
                    )
                    .hasRole("ADMIN")

                    // User Routes
                    .requestMatchers(
                            "/api/user/**"
                    )
                    .hasAnyRole(
                            "CUSTOMER",
                            "ADMIN"
                    )

                    // Any Other Request
                    .anyRequest()

                    .authenticated()
            )

            // Add JWT Filter
            .addFilterBefore(
                    jwtFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}