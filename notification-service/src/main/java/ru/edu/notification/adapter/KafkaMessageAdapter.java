package ru.edu.notification.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.edu.notification.model.TaskEvent;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageAdapter {
    
    // Внедряем ObjectMapper через конструктор
    private final ObjectMapper objectMapper;
    
    public TaskEvent adapt(String message) {
        try {
            // Пробуем распарсить как стандартное TaskEvent
            return objectMapper.readValue(message, TaskEvent.class);
        } catch (Exception e) {
            log.debug("Сообщение не является стандартным TaskEvent, пробуем адаптировать: {}", message);
            return adaptFromOtherFormat(message);
        }
    }
    
    public TaskEvent adaptFromOtherFormat(String message) {
        try {
            JsonNode node = objectMapper.readTree(message);
            
            String eventType = determineEventType(node);
            Long taskId = extractTaskId(node);
            Long employeeId = extractEmployeeId(node);
            String taskTitle = extractTaskTitle(node, taskId);
            String employeeName = extractEmployeeName(node, employeeId);
            
            return new TaskEvent(
                eventType,
                taskId,
                employeeId,
                taskTitle,
                employeeName,
                Instant.now()
            );
        } catch (Exception e) {
            log.error("Не удалось адаптировать сообщение: {}", message, e);
            // Возвращаем событие неизвестного типа
            return new TaskEvent(
                "UNKNOWN_EVENT",
                0L,
                0L,
                "Неизвестное событие",
                "Система",
                Instant.now()
            );
        }
    }
    
    private String determineEventType(JsonNode node) {
        if (node.has("eventType")) {
            return node.get("eventType").asText();
        }
        if (node.has("assignedAt")) {
            return "TASK_ASSIGNED";
        }
        if (node.has("completedAt")) {
            return "TASK_COMPLETED";
        }
        if (node.has("createdAt")) {
            return "TASK_CREATED";
        }
        if (node.has("id") && node.has("employeeId")) {
            return "TASK_ASSIGNED";
        }
        return "UNKNOWN_EVENT";
    }
    
    private Long extractTaskId(JsonNode node) {
        if (node.has("taskId")) {
            return node.get("taskId").asLong();
        }
        if (node.has("id")) {
            return node.get("id").asLong();
        }
        return 0L;
    }
    
    private Long extractEmployeeId(JsonNode node) {
        if (node.has("employeeId")) {
            return node.get("employeeId").asLong();
        }
        return 0L;
    }
    
    private String extractTaskTitle(JsonNode node, Long taskId) {
        if (node.has("taskTitle")) {
            return node.get("taskTitle").asText();
        }
        if (node.has("title")) {
            return node.get("title").asText();
        }
        if (taskId != 0) {
            return "Задача #" + taskId;
        }
        return "Неизвестная задача";
    }
    
    private String extractEmployeeName(JsonNode node, Long employeeId) {
        if (node.has("employeeName")) {
            return node.get("employeeName").asText();
        }
        if (node.has("name")) {
            return node.get("name").asText();
        }
        if (employeeId != 0) {
            return "Сотрудник #" + employeeId;
        }
        return "Система";
    }
}
