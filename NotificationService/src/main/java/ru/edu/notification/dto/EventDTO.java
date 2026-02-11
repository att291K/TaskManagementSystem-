package ru.edu.notification.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO для передачи данных о событии через REST API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для события")
public class EventDTO {

    @Schema(description = "ID события", example = "1")
    private Long id;

    @Schema(description = "Тип события", example = "TASK_ASSIGNED")
    private String eventType;

    @Schema(description = "ID задачи", example = "123")
    private Long taskId;

    @Schema(description = "ID сотрудника", example = "456")
    private Long employeeId;

    @Schema(description = "Сообщение события", example = "Задача 'Сделать отчет' назначена на Ивана Иванова")
    private String message;

    @Schema(description = "Дата и время создания события")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    // Статический метод для преобразования Entity в DTO
    public static EventDTO fromEntity(ru.edu.notification.model.EventLog eventLog) {
        return new EventDTO(
                eventLog.getId(),
                eventLog.getEventType(),
                eventLog.getTaskId(),
                eventLog.getEmployeeId(),
                eventLog.getMessage(),
                eventLog.getCreatedAt()
        );
    }
}
