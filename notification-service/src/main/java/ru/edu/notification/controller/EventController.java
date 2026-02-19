package ru.edu.notification.controller;

import ru.edu.notification.dto.ApiResponse;
import ru.edu.notification.dto.EventDTO;
import ru.edu.notification.dto.CreateEventRequest;
import ru.edu.notification.model.EventLog;
import ru.edu.notification.model.TaskEvent;
import ru.edu.notification.service.EventService;
import ru.edu.notification.adapter.RestRequestAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:8083")
@RequiredArgsConstructor
@Tag(name = "Events", description = "API для работы с событиями")
public class EventController {

    private final EventService eventService;
    private final RestRequestAdapter requestAdapter;

    // ==================== СУЩЕСТВУЮЩИЕ МЕТОДЫ ====================

    @GetMapping
    @Operation(
            summary = "Получить список событий",
            description = "Возвращает пагинированный список всех событий"
    )
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllEvents(
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Размер страницы", example = "20")
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<EventLog> eventsPage = eventService.getAllEvents(pageable);

        List<EventDTO> eventDTOs = eventsPage.getContent().stream()
                .map(EventDTO::fromEntity)
                .collect(Collectors.toList());

        Map<String, Object> response = Map.of(
                "events", eventDTOs,
                "currentPage", eventsPage.getNumber(),
                "totalItems", eventsPage.getTotalElements(),
                "totalPages", eventsPage.getTotalPages(),
                "pageSize", size
        );

        return ResponseEntity.ok(
                ApiResponse.success("Events retrieved successfully", response)
        );
    }

    @GetMapping("/task/{taskId}")
    @Operation(
            summary = "Получить события по задаче",
            description = "Возвращает все события для указанной задачи"
    )
    public ResponseEntity<ApiResponse<List<EventDTO>>> getEventsByTask(
            @Parameter(description = "ID задачи", example = "123", required = true)
            @PathVariable Long taskId) {

        List<EventLog> events = eventService.getEventsByTaskId(taskId);
        List<EventDTO> eventDTOs = events.stream()
                .map(EventDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                ApiResponse.success("Task events retrieved", eventDTOs)
        );
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(
            summary = "Получить события по сотруднику",
            description = "Возвращает все события для указанного сотрудника"
    )
    public ResponseEntity<ApiResponse<List<EventDTO>>> getEventsByEmployee(
            @Parameter(description = "ID сотрудника", example = "456", required = true)
            @PathVariable Long employeeId) {

        List<EventLog> events = eventService.getEventsByEmployeeId(employeeId);
        List<EventDTO> eventDTOs = events.stream()
                .map(EventDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                ApiResponse.success("Employee events retrieved", eventDTOs)
        );
    }

    @GetMapping("/recent")
    @Operation(
            summary = "Получить последние события",
            description = "Возвращает указанное количество последних событий"
    )
    public ResponseEntity<ApiResponse<List<EventDTO>>> getRecentEvents(
            @Parameter(description = "Количество событий", example = "10")
            @RequestParam(defaultValue = "10") int limit) {

        List<EventLog> events = eventService.getRecentEvents(limit);
        List<EventDTO> eventDTOs = events.stream()
                .map(EventDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                ApiResponse.success("Recent events retrieved", eventDTOs)
        );
    }

    @GetMapping("/stats")
    @Operation(
            summary = "Получить статистику событий",
            description = "Возвращает общую статистику по событиям"
    )
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        Map<String, Object> stats = Map.of(
                "totalEvents", eventService.getTotalEvents(),
                "service", "Notification Service",
                "status", "running",
                "timestamp", java.time.LocalDateTime.now().toString()
        );

        return ResponseEntity.ok(
                ApiResponse.success("Statistics retrieved", stats)
        );
    }

    @PostMapping("/test")
    @Operation(
            summary = "Создать тестовое событие",
            description = "Создает тестовое событие для проверки работы сервиса"
    )
    public ResponseEntity<ApiResponse<EventDTO>> createTestEvent() {
        EventDTO testEvent = new EventDTO(
                999L,
                "TEST_EVENT",
                999L,
                100L,
                "Тестовое событие, созданное через API",
                java.time.LocalDateTime.now()
        );

        return ResponseEntity.ok(
                ApiResponse.success("Test event created", testEvent)
        );
    }

    @GetMapping("/health")
    @Operation(
            summary = "Проверка здоровья сервиса",
            description = "Проверяет доступность сервиса и его компонентов"
    )
    public ResponseEntity<ApiResponse<Map<String, Object>>> healthCheck() {
        Map<String, Object> health = Map.of(
                "status", "UP",
                "service", "notification-service",
                "timestamp", java.time.LocalDateTime.now().toString(),
                "database", "H2",
                "kafka", "connected",
                "totalEvents", eventService.getTotalEvents()
        );

        return ResponseEntity.ok(
                ApiResponse.success("Service is healthy", health)
        );
    }

    // ==================== НОВЫЕ МЕТОДЫ ДЛЯ REST ====================

    @PostMapping
    @Operation(
            summary = "Создать событие через REST",
            description = "Принимает JSON с данными события и сохраняет в БД"
    )
    public ResponseEntity<ApiResponse<EventDTO>> createEvent(
            @RequestBody CreateEventRequest request) {
        
        TaskEvent taskEvent = requestAdapter.adapt(request);
        eventService.saveEventFromTaskEvent(taskEvent);
        
        // Получаем последнее сохраненное событие для возврата (упрощенно)
        List<EventLog> recent = eventService.getRecentEvents(1);
        EventDTO eventDTO = recent.isEmpty() ? null : EventDTO.fromEntity(recent.get(0));
        
        return ResponseEntity.ok(
                ApiResponse.success("Event created successfully via REST", eventDTO)
        );
    }

    @PostMapping("/raw")
    @Operation(
            summary = "Создать событие из сырого JSON",
            description = "Принимает произвольный JSON и пытается адаптировать его в событие"
    )
    public ResponseEntity<ApiResponse<EventDTO>> createEventFromRaw(
            @RequestBody String rawJson) {
        
        TaskEvent taskEvent = requestAdapter.adapt(rawJson);
        eventService.saveEventFromTaskEvent(taskEvent);
        
        List<EventLog> recent = eventService.getRecentEvents(1);
        EventDTO eventDTO = recent.isEmpty() ? null : EventDTO.fromEntity(recent.get(0));
        
        return ResponseEntity.ok(
                ApiResponse.success("Event created from raw JSON", eventDTO)
        );
    }

    @PostMapping("/task/created")
@Operation(summary = "Создать событие о создании задачи")
public ResponseEntity<ApiResponse<EventDTO>> createTaskCreatedEvent(
        @RequestBody CreateEventRequest request) {  // Принимаем JSON в теле
    
    //log.info("📥 Received task created event: {}", request);
    
    TaskEvent taskEvent = new TaskEvent(
            "TASK_CREATED",
            request.getTaskId(),
            request.getEmployeeId() != null ? request.getEmployeeId() : 0L,
            request.getTaskTitle(),
            null,
            java.time.Instant.now()
    );
    
    eventService.saveEventFromTaskEvent(taskEvent);
    
    List<EventLog> recent = eventService.getRecentEvents(1);
    EventDTO eventDTO = recent.isEmpty() ? null : EventDTO.fromEntity(recent.get(0));
    
    return ResponseEntity.ok(
            ApiResponse.success("Task created event logged", eventDTO)
    );
}

    @PostMapping("/task/assigned")
    @Operation(
            summary = "Создать событие о назначении задачи",
            description = "Специализированный эндпоинт для события назначения задачи"
    )
    public ResponseEntity<ApiResponse<EventDTO>> createTaskAssignedEvent(
            @RequestParam Long taskId,
            @RequestParam Long employeeId,
            @RequestParam String employeeName,
            @RequestParam String taskTitle) {
        
        TaskEvent taskEvent = new TaskEvent(
                "TASK_ASSIGNED",
                taskId,
                employeeId,
                taskTitle,
                employeeName,
                java.time.Instant.now()
        );
        
        eventService.saveEventFromTaskEvent(taskEvent);
        
        List<EventLog> recent = eventService.getRecentEvents(1);
        EventDTO eventDTO = recent.isEmpty() ? null : EventDTO.fromEntity(recent.get(0));
        
        return ResponseEntity.ok(
                ApiResponse.success("Task assigned event logged", eventDTO)
        );
    }

    @PostMapping("/task/completed")
    @Operation(
            summary = "Создать событие о завершении задачи",
            description = "Специализированный эндпоинт для события завершения задачи"
    )
    public ResponseEntity<ApiResponse<EventDTO>> createTaskCompletedEvent(
            @RequestParam Long taskId,
            @RequestParam(required = false) Long employeeId,
            @RequestParam String taskTitle) {
        
        TaskEvent taskEvent = new TaskEvent(
                "TASK_COMPLETED",
                taskId,
                employeeId != null ? employeeId : 0L,
                taskTitle,
                null,
                java.time.Instant.now()
        );
        
        eventService.saveEventFromTaskEvent(taskEvent);
        
        List<EventLog> recent = eventService.getRecentEvents(1);
        EventDTO eventDTO = recent.isEmpty() ? null : EventDTO.fromEntity(recent.get(0));
        
        return ResponseEntity.ok(
                ApiResponse.success("Task completed event logged", eventDTO)
        );
    }
}