package ru.edu.authservice.service.impl;
import ru.edu.authservice.model.User;
import ru.edu.authservice.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @PostConstruct
    public void fillDatabase() {
        User user;
        try {
            user = new User();
            user.setUsername("testUser");
            user.setPassword(passwordEncoder.encode("testPassword"));
            user.setRoles(Collections.singleton("MANAGER"));
            userRepository.save(user);
        }
        catch (Exception ignored) {}

        try {
            user = new User();
            user.setUsername("Smith");
            user.setPassword(passwordEncoder.encode("Smith"));
            user.setRoles(Collections.singleton("MANAGER"));
            userRepository.save(user);
        }
        catch (Exception ignored) {}

        try {
            user = new User();
            user.setUsername("Petrov");
            user.setPassword(passwordEncoder.encode("Petrov"));
            user.setRoles(Collections.singleton("EMPLOYEE"));
            userRepository.save(user);
        }
        catch (Exception ignored) {}

        try {
            user = new User();
            user.setUsername("Ivanov");
            user.setPassword(passwordEncoder.encode("Petrov"));
            user.setRoles(Collections.singleton("EMPLOYEE"));
            userRepository.save(user);
        }
        catch (Exception ignored) {}

        try {
            user = new User();
            user.setUsername("Sidorov");
            user.setPassword(passwordEncoder.encode("Petrov"));
            user.setRoles(Collections.singleton("EMPLOYEE"));
            userRepository.save(user);
        }
        catch (Exception ignored) {}
    }
}
