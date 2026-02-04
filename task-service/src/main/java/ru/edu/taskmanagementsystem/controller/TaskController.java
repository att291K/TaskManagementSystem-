package ru.edu.taskmanagementsystem.controller;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.edu.taskmanagementsystem.dto.TaskDtoRequest;
import ru.edu.taskmanagementsystem.dto.TaskDtoResponse;
import ru.edu.taskmanagementsystem.mapper.TaskMapper;
import ru.edu.taskmanagementsystem.model.TaskM;
import ru.edu.taskmanagementsystem.model.User;
import ru.edu.taskmanagementsystem.service.TaskService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

//import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GetMapping("/getAllTasks")
    public ResponseEntity<@NonNull List<TaskDtoResponse>> getAllEvents() {
        User principal = (User) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        System.out.println(principal);
        return ResponseEntity.ok(taskMapper.toDto(taskService.findAll()));
    }

    @PostMapping("/createTask")
    public TaskM assign(@RequestBody TaskDtoRequest taskRequest) {
        return taskService.createTask(taskMapper.toTask(taskRequest));
    }

    @GetMapping("/getTaskById/{id}")
    public ResponseEntity<@NonNull TaskDtoResponse> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(taskMapper.toDto(taskService.findById(id)));
    }

    @DeleteMapping("/deleteTaskById/{id}")
    public void deleteEventById(@PathVariable Long id) {
        taskService.deleteById(id);
    }

    @DeleteMapping("/deleteAllTasks")
    public void deleteAllEvents() {
        taskService.deleteAll();
    }

    @GetMapping("/existsTaskById/{id}")
    public ResponseEntity<@NonNull Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.existsById(id));
    }
}