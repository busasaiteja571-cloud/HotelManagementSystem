package com.example.HotelManagementSystem.security;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;



@Component
public class JwtUtil {

    private final String SECRET = 
            "hospitalsecretkeyhospitalsecretkey123";

    private final Key key =
            Keys.hmacShaKeyFor(SECRET.getBytes());

    // GENERATE TOKEN

    public String generateToken(String username) {

        return Jwts.builder()

                .setSubject(username)

                .setIssuedAt(new Date())

                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000 * 60 * 60
                        )
                )

                .signWith(key)

                .compact();
    }

    // EXTRACT USERNAME

    public String extractUsername(String token) {

        return Jwts.parserBuilder()

                .setSigningKey(key)

                .build()

                .parseClaimsJws(token)

                .getBody()

                .getSubject();
    }
}
