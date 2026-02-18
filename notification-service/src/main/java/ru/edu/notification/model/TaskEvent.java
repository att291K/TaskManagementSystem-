package ru.edu.notification.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

// Простая DTO для событий из Kafka

public record TaskEvent(
        String eventType,
        Long taskId,
        Long employeeId,
        String taskTitle,
        String employeeName,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
        Instant timestamp
) {

    public EventLog toEventLog() {
        String message = String.format("%s: задача '%s' (ID: %d)",
                getEventDescription(),
                taskTitle != null ? taskTitle : "Без названия",
                taskId);

        return new EventLog(
                eventType,
                taskId,
                employeeId,
                message
        );
    }

    private String getEventDescription() {
        return switch (eventType) {
            case "TASK_CREATED" -> "Задача создана";
            case "TASK_ASSIGNED" -> "Задача назначена";
            case "STATUS_CHANGED" -> "Статус изменен";
            case "TASK_COMPLETED" -> "Задача завершена";
            default -> "Событие";
        };
    }
}
