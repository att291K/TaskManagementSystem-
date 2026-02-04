package ru.edu.taskmanagementsystem.service;

public interface JwtUtils {
    public String generateToken(String username);
    public String extractUsername(String token);
}
