package ru.edu.taskmanagementsystem.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.edu.taskmanagementsystem.dto.TaskDtoRequest;
import ru.edu.taskmanagementsystem.model.TaskM;
import ru.edu.taskmanagementsystem.service.TaskService;

//import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/createTask")
    public TaskM assign(@RequestBody TaskDtoRequest taskRequest) {
        TaskM task = new TaskM();

        task.setDateOfCreate(java.time.LocalDateTime.now());
        var requestValue = taskRequest.getValue();
        //var dataFromFront = objectMapper.readValue(requestValue, Dto.class);
        //task.setAuthor(dataFromFront.Author);
        task.setId(Long.parseLong(taskRequest.getId()));

        return taskService.createTask(task);
    }

    /*private final TaskMapper taskMapper;

    @GetMapping("/getAllEvents")
    public ResponseEntity<@NonNull List<TaskDtoResponce>> getAllEvents() {
        return ResponseEntity.ok(taskMapper.toDto(taskService.findAll()));
    }

    @GetMapping("/getEventById/{id}")
    public ResponseEntity<@NonNull TaskDtoResponse> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(taskMapper.toDto(taskService.findById(id)));
    }

    @DeleteMapping("/deleteEventById/{id}")
    public void deleteEventById(@PathVariable Long id) {
        taskService.deleteById(id);
    }

    @DeleteMapping("/deleteAllEvents")
    public void deleteAllEvents() {
        taskService.deleteAll();
    }

    @PostMapping "/createEvent")
    public ResponseEntity<@NonNull TaskDtoResponse> createEvent(@RequestBody TaskDtoRequest request) {
        return ResponseEntity.ok(taskMapper
                .toDto(taskService.createEvent(request.getTitle(), LocalDateTime.now().toString())));
    }

    @GetMapping("/existsById/{id}")
    public ResponseEntity<@NonNull Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.existsById(id));
    }*/
}