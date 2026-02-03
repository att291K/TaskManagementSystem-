package ru.edu.taskmanagementsystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Entity
@Data
@Table (name = "users_table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;
    //@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = false)
    //private List<TaskM> tasks;
    //@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Comment> comments;
}

