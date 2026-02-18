package ru.edu.taskmanagementsystem.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtUtils {
    public String generateToken(String username, String role);
    public String extractUsername(String token);
    public Claims extractAllClaims(String token);

}
