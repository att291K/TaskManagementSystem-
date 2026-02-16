package ru.edu.authservice.controller;

import ru.edu.authservice.service.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Set;

@RestController
@AllArgsConstructor
public class GetRolesController {
    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;

    @PostMapping("/getRoles")
    public ResponseEntity<?> getRolesFromToken(@RequestBody @NonNull String json) {
        JsonNode node = objectMapper.readTree(json);
        String token = node.get("token").asText(null);

        if (token == null) {
            return ResponseEntity.badRequest().body("No token");
        }

        Set<String> roles = jwtUtils.extractRoles(token);

        return ResponseEntity.ok().body(roles);
    }
}
