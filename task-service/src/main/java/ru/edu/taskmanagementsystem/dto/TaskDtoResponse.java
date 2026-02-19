package ru.edu.taskmanagementsystem.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class TaskDtoResponse{

    private Long id;
    private String title;
    private String description;
    private String status;
    private String dateTime;
}
