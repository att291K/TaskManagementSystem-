package authservice.service;

import authservice.model.User;

import java.util.List;
import java.util.Set;

public interface JwtUtils {
    public String generateToken(User user);
    public String extractUsername(String token);
    public Set<String> extractRoles(String token);
    public boolean validateToken(String token);
}
