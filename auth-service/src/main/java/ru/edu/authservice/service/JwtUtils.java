package ru.edu.authservice.service;

import ru.edu.authservice.model.User;

import java.util.Set;

public interface JwtUtils {
    public String generateToken(User user);
    public String extractUsername(String token);
    public Set<String> extractRoles(String token);
    public boolean validateToken(String token);
}
