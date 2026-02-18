package ru.edu.authservice.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.edu.authservice.dto.AuthRequestDto;
import ru.edu.authservice.dto.AuthResponseDto;
import ru.edu.authservice.dto.UserDto;
import ru.edu.authservice.model.User;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.edu.authservice.repository.UserRepository;
import ru.edu.authservice.service.JwtUtils;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class AuthController {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @PostMapping("/api/auth/login")
    public ResponseEntity<@NonNull AuthResponseDto> login(@RequestBody @NonNull AuthRequestDto authRequestDto) {
        String username = authRequestDto.getUsername();
        String password = authRequestDto.getPassword();
        System.out.println("username: " + username);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        User user = (User) authentication.getPrincipal();

        AuthResponseDto response = new AuthResponseDto();
        response.setToken(jwtUtils.generateToken(user));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userRepository.findAll().stream()
                .map(user -> {
                    UserDto dto = new UserDto();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUsername());
                    // Извлекаем названия ролей (например, "ROLE_USER")
                    dto.setRoles(user.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toSet()));
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }
}