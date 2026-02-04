package ru.edu.taskmanagementsystem.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    public String username;
    public Set<String> roles;
}
