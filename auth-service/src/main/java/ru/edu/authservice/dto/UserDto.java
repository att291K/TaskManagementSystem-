package ru.edu.authservice.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private Long id;
    public String username;
    public Set<String> roles;
}
