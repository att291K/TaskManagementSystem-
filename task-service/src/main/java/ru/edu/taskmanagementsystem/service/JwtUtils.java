package ru.edu.taskmanagementsystem.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtUtils {
    public String generateToken(String username, String role);
    public String extractUsername(String token);

}
