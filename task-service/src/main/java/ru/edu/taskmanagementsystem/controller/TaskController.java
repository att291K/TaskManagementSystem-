package ru.edu.taskmanagementsystem.controller;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.edu.taskmanagementsystem.dto.TaskDtoRequest;
import ru.edu.taskmanagementsystem.dto.TaskDtoResponse;
import ru.edu.taskmanagementsystem.mapper.TaskMapper;
import ru.edu.taskmanagementsystem.model.TaskM;
import ru.edu.taskmanagementsystem.model.enums.Status;
import ru.edu.taskmanagementsystem.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GetMapping("/getAllTasks")
    public ResponseEntity<List<TaskDtoResponse>> getAllTasks() {
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

    @PatchMapping("/updateTask/{id}")
    public ResponseEntity<?> updateTaskStatus(
            @PathVariable Long id,
            @RequestBody TaskDtoRequest taskRequest) {

        System.out.println("Получен статус из JS: [" + taskRequest.getStatus() + "]"); // СМОТРИ ЭТО В ЛОГАХ

        TaskM task = taskService.findById(id);
        if (task == null) return ResponseEntity.notFound().build();

        try {
            // Убедись, что STATUS_OPTIONS в JS и Enum в Java идентичны
            String statusFromJs = taskRequest.getStatus().trim().toUpperCase();
            task.setStatus(Status.valueOf(statusFromJs));

            return ResponseEntity.ok(taskService.updateTask(task));
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: статус '" + taskRequest.getStatus() + "' не найден в Enum!");
            return ResponseEntity.badRequest().body("Invalid status value");
        }
    }
}