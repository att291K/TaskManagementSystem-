package ru.edu.taskmanagementsystem.mapper;

import jdk.jshell.Snippet;
import org.springframework.stereotype.Component;
import ru.edu.taskmanagementsystem.dto.TaskDtoRequest;
import ru.edu.taskmanagementsystem.dto.TaskDtoResponse;
import ru.edu.taskmanagementsystem.dto.UserDto;
import ru.edu.taskmanagementsystem.model.TaskM;
import ru.edu.taskmanagementsystem.model.User;
import ru.edu.taskmanagementsystem.model.enums.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

     public UserDto toDto(User task) {
            UserDto response = new UserDto();
            response.setUsername(task.getUsername());
            response.setRoles(task.getRoles());
            return response;
        }

        public User toUser(UserDto request) {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setRoles(request.getRoles());
            return user;
        }

    public List<UserDto> toDto(Iterable<User> users) {
        List<UserDto> responses = new ArrayList<>();

        users.forEach(user -> {
            UserDto response = new UserDto();
            response.setUsername(user.getUsername());
            response.setRoles(user.getRoles());
            responses.add(response);
        });

        return responses;
    }
}
