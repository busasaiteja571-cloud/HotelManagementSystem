package com.example.HotelManagementSystem.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.example.HotelManagementSystem.security.TokenBlacklistService;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Component
public class JwtFilter extends OncePerRequestFilter {

    // =========================
    // DEPENDENCIES
    // =========================

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    private final TokenBlacklistService tokenBlacklistService;

    // =========================
    // CONSTRUCTOR
    // =========================

    public JwtFilter(
            JwtUtil jwtUtil,
            UserDetailsService userDetailsService,
            TokenBlacklistService tokenBlacklistService
    ) {

        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    // =========================
    // FILTER LOGIC
    // =========================

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader =
                request.getHeader("Authorization");

        String jwt = null;

        String username = null;

        // =========================
        // CHECK HEADER
        // =========================

        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);

            return;
        }

        // =========================
        // EXTRACT TOKEN
        // =========================

        jwt = authHeader.substring(7);

        // =========================
        // EXTRACT USERNAME
        // =========================

        try {

            username = jwtUtil.extractUsername(jwt);

        } catch (Exception e) {

            System.out.println("Invalid JWT Token");

            filterChain.doFilter(request, response);

            return;
        }

        // =========================
        // AUTHENTICATE USER
        // =========================

        if (username != null
                && SecurityContextHolder.getContext()
                .getAuthentication() == null) {

            UserDetails userDetails =
                    userDetailsService
                            .loadUserByUsername(username);

            if (!tokenBlacklistService.isTokenRevoked(jwt)
                    && jwtUtil.validateToken(
                            jwt,
                            userDetails.getUsername()
                    )) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}