package ru.edu.taskmanagementsystem.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public abstract class TaskDto {
    @NonNull
    private String id;
    @NonNull
    private String value;
    @NonNull
    private String dateTime;
}
