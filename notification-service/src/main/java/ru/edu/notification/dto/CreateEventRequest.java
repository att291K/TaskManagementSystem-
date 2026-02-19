package ru.edu.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на создание события через REST")
public class CreateEventRequest {
    
    @Schema(description = "Тип события", example = "TASK_CREATED", required = true)
    private String eventType;
    
    @Schema(description = "ID задачи", example = "123")
    private Long taskId;
    
    @Schema(description = "ID сотрудника", example = "456")
    private Long employeeId;
    
    @Schema(description = "Название задачи", example = "Создать отчет")
    private String taskTitle;
    
    @Schema(description = "Имя сотрудника", example = "Иван Иванов")
    private String employeeName;
}
