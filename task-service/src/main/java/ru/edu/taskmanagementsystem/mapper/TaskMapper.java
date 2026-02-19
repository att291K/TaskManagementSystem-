package ru.edu.taskmanagementsystem.mapper;

import org.springframework.stereotype.Component;
import ru.edu.taskmanagementsystem.dto.TaskDtoRequest;
import ru.edu.taskmanagementsystem.dto.TaskDtoResponse;
import ru.edu.taskmanagementsystem.model.enums.Status;
import ru.edu.taskmanagementsystem.model.TaskM;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TaskMapper {

    public TaskDtoResponse toDto(TaskM task) {
        TaskDtoResponse response = new TaskDtoResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(String.valueOf(task.getStatus()));
        response.setDateTime(String.valueOf(LocalDateTime.now()));
        return response;
    }

    public TaskM toTask(TaskDtoRequest request) {
        TaskM task = new TaskM();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());

        if (request.getStatus() != null) {
            try {
                task.setStatus(Status.valueOf(request.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                task.setStatus(Status.OPEN);
            }
        } else {
            task.setStatus(Status.OPEN);
        }

        return task;
    }

    public List<TaskDtoResponse> toDto(Iterable<TaskM> taskMs) {
        List<TaskDtoResponse> responses = new ArrayList<>();

        taskMs.forEach(task -> {
            TaskDtoResponse response = new TaskDtoResponse();
            response.setId(task.getId());
            response.setTitle(task.getTitle());
            response.setDescription(task.getDescription());
            response.setStatus(String.valueOf(task.getStatus()));
            response.setDateTime(String.valueOf(task.getDateOfCreate()));
            responses.add(response);
        });

        return responses;
    }
}
