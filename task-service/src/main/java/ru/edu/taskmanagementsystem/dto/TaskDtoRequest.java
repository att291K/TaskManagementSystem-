package ru.edu.taskmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDtoRequest {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String dateTime;
}
