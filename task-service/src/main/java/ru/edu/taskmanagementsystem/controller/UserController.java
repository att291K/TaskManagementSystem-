package ru.edu.taskmanagementsystem.controller;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.edu.taskmanagementsystem.dto.TaskDtoResponse;
import ru.edu.taskmanagementsystem.dto.UserDto;
import ru.edu.taskmanagementsystem.mapper.UserMapper;
import ru.edu.taskmanagementsystem.model.User;
import ru.edu.taskmanagementsystem.service.impl.UserDetailsServiceImpl;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserDetailsServiceImpl userService;
    private final UserMapper userMapper;

    @GetMapping("/getAllUsers")
    public ResponseEntity<@NonNull List<UserDto>> getAllTasks() {
        User principal = (User) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        System.out.println(principal);
        return ResponseEntity.ok(userMapper.toDto(userService.findAll()));
    }
}
