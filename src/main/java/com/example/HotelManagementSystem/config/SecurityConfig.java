package com.example.HotelManagementSystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.HotelManagementSystem.security.JwtFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    // ADD THIS
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http

            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->

                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )

            .authorizeHttpRequests(auth -> auth

                    .requestMatchers(
                            "/api/auth/**"
                    )
                    .permitAll()

                    .requestMatchers(
                            "/api/admin/**"
                    )
                    .hasRole("ADMIN")

                    .requestMatchers(
                            "/api/staff/**"
                    )
                    .hasRole("STAFF")

                    .requestMatchers(
                            "/api/user/**"
                    )
                    .hasAnyRole(
                            "CUSTOMER",
                            "ADMIN"
                    )

                    .anyRequest()
                    .authenticated()
            )

            .addFilterBefore(
                    jwtFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}