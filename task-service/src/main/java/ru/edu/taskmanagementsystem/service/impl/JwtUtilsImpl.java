package ru.edu.taskmanagementsystem.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import ru.edu.taskmanagementsystem.service.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtUtilsImpl implements JwtUtils {

    private final String SECRET_KEY = "mySuperSecretKeyForJwtThatIsAtLeast256BitsLong!!!";

    @Override
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getPayload()
                .getSubject();
    }


}
