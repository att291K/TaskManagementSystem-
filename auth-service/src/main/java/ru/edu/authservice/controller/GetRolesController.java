package ru.edu.authservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import ru.edu.authservice.service.JwtUtils;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@AllArgsConstructor
public class GetRolesController {
    private final JwtUtils jwtUtils;

    @GetMapping("/getRoles")
    public ResponseEntity<?> getRolesFromToken(@NonNull HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);

            Set<String> roles = jwtUtils.extractRoles(token);
            return ResponseEntity.ok().body(roles);
        }

        return ResponseEntity.status(401).body(null);
    }
}
