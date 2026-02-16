package ru.edu.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Стандартный ответ API для всех эндпоинтов
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Стандартный ответ API")
public class ApiResponse<T> {

    @Schema(description = "Статус операции", example = "success")
    private String status;

    @Schema(description = "Сообщение об операции", example = "Events retrieved successfully")
    private String message;

    @Schema(description = "Данные ответа")
    private T data;

    @Schema(description = "Временная метка")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    @Schema(description = "Код HTTP ответа", example = "200")
    private int code;

    // Статические методы для удобства создания ответов
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("success", message, data, LocalDateTime.now(), 200);
    }

    public static <T> ApiResponse<T> success(T data) {
        return success("Operation completed successfully", data);
    }

    public static <T> ApiResponse<T> error(String message, int code) {
        return new ApiResponse<>("error", message, null, LocalDateTime.now(), code);
    }

    public static <T> ApiResponse<T> error(String message, T data, int code) {
        return new ApiResponse<>("error", message, data, LocalDateTime.now(), code);
    }
}
