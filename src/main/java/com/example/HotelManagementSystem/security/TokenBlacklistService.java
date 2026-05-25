package com.example.HotelManagementSystem.security;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class TokenBlacklistService {

    private final Map<String, Date> revokedTokens =
            new ConcurrentHashMap<>();

    private final JwtUtil jwtUtil;

    public TokenBlacklistService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public void revokeToken(String token) {
        Date expiration = jwtUtil.extractExpiration(token);
        revokedTokens.put(token, expiration);
        removeExpiredTokens();
    }

    public boolean isTokenRevoked(String token) {
        removeExpiredTokens();
        return revokedTokens.containsKey(token);
    }

    private void removeExpiredTokens() {
        Date now = new Date();
        revokedTokens.entrySet().removeIf(
                entry -> entry.getValue().before(now)
        );
    }
}
