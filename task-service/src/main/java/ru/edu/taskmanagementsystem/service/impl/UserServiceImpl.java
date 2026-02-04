package ru.edu.taskmanagementsystem.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edu.taskmanagementsystem.model.User;
import ru.edu.taskmanagementsystem.repository.UserRepository;
import ru.edu.taskmanagementsystem.service.UserService;

import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User createUser(String login, String password, Set<String> roles) {
        User user = new User();
        user.setUsername(login);
        user.setPassword(password);
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.getReferenceById(id);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }
}
