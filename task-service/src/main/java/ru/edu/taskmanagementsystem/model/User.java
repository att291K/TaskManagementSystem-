package ru.edu.taskmanagementsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.edu.taskmanagementsystem.model.enums.Role;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@Table (name = "users_table")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, name = "username", nullable = false)
    private String username;
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;
    //private Role role;
    //@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = false)
    //private List<TaskM> tasks;
    //@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Comment> comments;

    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
    /*
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);

    }*/

    @Override
    @NonNull
    public String getUsername() {
        return username;
    }

}

