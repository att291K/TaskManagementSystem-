package ru.edu.taskmanagementsystem.dto;

import lombok.NonNull;

//@Data
public class TaskDtoRequest extends TaskDto {

    public TaskDtoRequest(@NonNull String id, @NonNull String value, @NonNull String dateTime) {
        super(id, value, dateTime);
    }
}
