package ru.edu.taskmanagementsystem.service;

import ru.edu.taskmanagementsystem.model.TaskM;
import ru.edu.taskmanagementsystem.model.User;

import java.util.Set;

public interface UserService {
    User createUser(String login, String password, Set<String> roles);

    default User createUser () {
        return new User();
    }

    User findById(Long id);

    void deleteById(Long id);

    void deleteAll();

    Iterable<User> findAll();

    boolean existsById(Long id);

    User createUser(User user);
}
