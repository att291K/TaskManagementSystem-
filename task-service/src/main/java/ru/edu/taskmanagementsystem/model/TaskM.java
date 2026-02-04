package ru.edu.taskmanagementsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import ru.edu.taskmanagementsystem.model.enums.Status;

import java.time.LocalDateTime;

@Entity
@Data
@Table (name = "tasks_table")
public class TaskM {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true, name = "title_of_task", nullable = false)
    private String title;
    @Column(name = "description_of_event", nullable = false)
    private String description;
    @Column(name = "status_of_event", nullable = false)
    private Status status;
    private LocalDateTime dateOfCreate;
    /*@ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;
    private LocalDateTime dateOfLastChange;
    private LocalDateTime dateOfClose;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;*/
}

