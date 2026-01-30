package ru.edu.taskmanagementsystem.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private long id;
    private String text;
    private User author;
    private LocalDateTime date;
}
