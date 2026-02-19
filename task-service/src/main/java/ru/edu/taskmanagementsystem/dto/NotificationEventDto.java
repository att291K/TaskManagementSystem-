package ru.edu.taskmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEventDto {
    private String eventType;
    private Long taskId;
    private Long employeeId;
    private String taskTitle;
    private String employeeName;
    
    // Для универсального эндпоинта можно добавить:
    private Object additionalData;
}
