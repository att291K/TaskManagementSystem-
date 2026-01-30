package ru.edu.taskmanagementsystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table (name = "tasks_table")
public class TaskM {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private String textOfTask;
    private Status status;
    @ManyToOne
    @JoinColumn(name = "author")
    private User author;
    private LocalDateTime dateOfCreate;
    private LocalDateTime dateOfLastChange;
    private LocalDateTime dateOfClose;
    private List<Comment> comment;
}
