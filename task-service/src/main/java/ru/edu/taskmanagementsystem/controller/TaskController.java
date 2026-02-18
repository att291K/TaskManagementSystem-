package ru.edu.taskmanagementsystem.controller;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.edu.taskmanagementsystem.dto.TaskDtoRequest;
import ru.edu.taskmanagementsystem.dto.TaskDtoResponse;
import ru.edu.taskmanagementsystem.mapper.TaskMapper;
import ru.edu.taskmanagementsystem.model.TaskM;
import ru.edu.taskmanagementsystem.model.User;
import ru.edu.taskmanagementsystem.service.TaskService;

import java.util.List;
import java.util.Objects;

//import com.fasterxml.jackson.databind.ObjectMapper;


@Controller
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GetMapping("/getAllTasks")
    public ResponseEntity<@NonNull List<TaskDtoResponse>> getAllTasks() {
        //User principal = (User) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        //System.out.println(principal);
        return ResponseEntity.ok(taskMapper.toDto(taskService.findAll()));
    }

    @PostMapping("/createTask")
    public TaskM createTask(@RequestBody TaskDtoRequest taskRequest) {
        return taskService.createTask(taskMapper.toTask(taskRequest));
    }

    @GetMapping("/getTaskById/{id}")
    public ResponseEntity<@NonNull TaskDtoResponse> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskMapper.toDto(taskService.findById(id)));
    }

    @DeleteMapping("/deleteTaskById/{id}")
    public void deleteTaskById(@PathVariable Long id) {
        taskService.deleteById(id);
    }

    @DeleteMapping("/deleteAllTasks")
    public void deleteAllTasks() {
        taskService.deleteAll();
    }

    @GetMapping("/existsTaskById/{id}")
    public ResponseEntity<@NonNull Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.existsById(id));
    }
}