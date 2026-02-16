package ru.edu.notification.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type", nullable = false)
    private String eventType;  // "TASK_CREATED", "TASK_ASSIGNED", etc.

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Простой конструктор
    public EventLog(String eventType, Long taskId, Long employeeId, String message) {
        this.eventType = eventType;
        this.taskId = taskId;
        this.employeeId = employeeId;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }
}